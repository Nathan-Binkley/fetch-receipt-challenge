package com.fetch.fetch.service;

import com.fetch.fetch.model.Item;
import com.fetch.fetch.model.Points;
import com.fetch.fetch.model.Receipt;
import com.fetch.fetch.model.ReceiptId;
import com.fetch.fetch.repository.ReceiptRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FetchServiceImpl implements FetchService{

    private final ReceiptRepository receiptRepository;

    private static final int ALPHA_NUMERIC_RETAILER_COUNT_POINTS = 1;
    private static final int ROUND_DOLLAR_AMOUNT_POINTS = 50;
    private static final int MULTIPLE_OF_TWENTY_FIVE_POINTS = 25;
    private static final int EVERY_TWO_ITEMS_POINTS = 5;
    private static final int ODD_PURCHASE_DAY_POINTS = 6;
    private static final int PURCHASE_TIME_POINTS = 10;

    public FetchServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public Points getPoints(String id) {
        log.info("Fetching points for receipt id: {}", id);
        return new Points(receiptRepository.getPoints(id).describeConstable().orElseThrow(() -> new ObjectNotFoundException(Receipt.class, id)));
    }

    @Override
    public ReceiptId postReceipt(Receipt receipt) {
        log.info("Posting receipt: {}", receipt);

        if (receipt.getRetailer() == null) {
            throw new IllegalArgumentException("Retailer cannot be null");
        }
        if (receipt.getPurchaseDate() == null) {
            throw new IllegalArgumentException("Purchase date cannot be null");
        }
        if (receipt.getPurchaseTime() == null) {
            throw new IllegalArgumentException("Purchase time cannot be null");
        }
        if (receipt.getItems() == null) {
            throw new IllegalArgumentException("Items cannot be null");
        }
        if (receipt.getTotal() == null) {
            throw new IllegalArgumentException("Total cannot be null");
        }

        Integer points = 0;
        Double total = Double.parseDouble(receipt.getTotal());

        String retailer = receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "");
        points += retailer.length() * ALPHA_NUMERIC_RETAILER_COUNT_POINTS;

        log.info("Points after ALPHA: {}", points);
        if(total == Math.floor(total))
        {
            points += ROUND_DOLLAR_AMOUNT_POINTS;
        }
        log.info("Points after ROUND DOLLAR: {}", points);


        if (total % 0.25 == 0)
        {
            points += MULTIPLE_OF_TWENTY_FIVE_POINTS;
        }
        log.info("Points AFTER MULTIPLE OF 25: {}", points);

        points += EVERY_TWO_ITEMS_POINTS * (int)(Math.floor(receipt.getItems().size() / 2));
        log.info("Points AFTER EVERY TWO ITEMS: {}", points);

        for(Item item : receipt.getItems())
            if(item.getShortDescription().trim().length() % 3 == 0)
                points += (int)Math.ceil((Double.valueOf(item.getPrice()) * 0.2));
        log.info("Points AFTER BUSINESS JARGON: {}", points);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(receipt.getPurchaseDate());
            Integer day = date.getDate();
            log.info("Day: {}", date.getDate());
            if(day % 2 != 0)
                points += ODD_PURCHASE_DAY_POINTS;
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid purchase date");
        }
        log.info("Points AFTER PURCHASE DAY: {}", points);

        LocalTime t = LocalTime.parse(receipt.getPurchaseTime());

        if((t.getHour() >=14 && t.getMinute() >=1) && (t.getHour() <= 15 && t.getMinute() <= 59))
            points += PURCHASE_TIME_POINTS;

        log.info("Final points: {}", points);

        receipt.setPoints(String.valueOf(points));

        Receipt saved = receiptRepository.save(receipt);

        return new ReceiptId(saved.getId());
    }
}
