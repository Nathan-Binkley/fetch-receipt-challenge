package com.fetch.fetch.service;

import com.fetch.fetch.model.Points;
import com.fetch.fetch.model.Receipt;
import com.fetch.fetch.model.ReceiptId;
import org.springframework.stereotype.Service;

public interface FetchService {

    Points getPoints(String id);

    ReceiptId postReceipt(Receipt receipt);

}
