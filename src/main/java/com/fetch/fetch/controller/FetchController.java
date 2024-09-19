package com.fetch.fetch.controller;

import com.fetch.fetch.model.Points;
import com.fetch.fetch.model.Receipt;
import com.fetch.fetch.model.ReceiptId;
import com.fetch.fetch.service.FetchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/receipts")
public class FetchController {

    private final FetchService fetchService;

    public FetchController(FetchService fetchService) {
        this.fetchService = fetchService;
    }

    @GetMapping({"{id}/points", "{id}/points/"})
    public ResponseEntity<Points> getPoints(@PathVariable String id) {
        log.info("Fetching points for receipt id: {}", id);

        Points points = fetchService.getPoints(id);

        return ResponseEntity.ok(points);
    }

    @PostMapping({"process", "process/"})
    public ResponseEntity<ReceiptId> postReceipt(@RequestBody Receipt receipt) {
        log.info("Posting receipt: {}", receipt);

        ReceiptId id = fetchService.postReceipt(receipt);

        return ResponseEntity.ok(id);
    }
}
