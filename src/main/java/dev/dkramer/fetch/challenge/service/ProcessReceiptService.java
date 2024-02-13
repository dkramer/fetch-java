package dev.dkramer.fetch.challenge.service;

import dev.dkramer.fetch.challenge.api.models.ProcessReceiptRequest;
import dev.dkramer.fetch.challenge.api.models.ReceiptItem;
import dev.dkramer.fetch.challenge.repository.ItemRepository;
import dev.dkramer.fetch.challenge.repository.ReceiptRepository;
import dev.dkramer.fetch.challenge.repository.models.Item;
import dev.dkramer.fetch.challenge.repository.models.Receipt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service that processes receipts.
 * Calculates points and then inserts into a DB.
 *
 * Could calculate points on retrievel but I went with on post/insert.
 */
@Slf4j
@Service
public class ProcessReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ItemRepository itemRepository;

    public String processReceipt(ProcessReceiptRequest processReceiptRequest) {

        Receipt receipt = createReceiptFromProcessRequest(processReceiptRequest);

        // normally I would probably have a receiptEntity backed by hibernate or JPA
        // which would include a lazy loaded one to many relationship with "item"
        // I wanted to show I actually know sql and to use prepared statements for SQL injection protection
        receiptRepository.insertNewReceipt(receipt);
        itemRepository.insertNewItems(receipt.getReceiptItems());

        log.info("Number of points {}", receipt.getPoints());
        return receipt.getReceiptId();
    }

    private Receipt createReceiptFromProcessRequest(ProcessReceiptRequest processReceiptRequest) {
        String receiptId = UUID.randomUUID().toString();
        return Receipt
                .builder()
                .receiptId(receiptId)
                .total(Double.parseDouble(processReceiptRequest.getTotal()))
                .purchaseTime(processReceiptRequest.getPurchaseTime())
                .purchaseDate(processReceiptRequest.getPurchaseDate())
                .retailer(processReceiptRequest.getRetailer())
                .receiptItems(processReceiptRequest.getItems().stream().map(receiptItem -> Item
                        .builder()
                                .receiptId(receiptId)
                                .price(Double.parseDouble(receiptItem.getPrice()))
                                .shortDescription(receiptItem.getShortDescription())
                                .itemId(UUID.randomUUID().toString())
                        .build())
                        .collect(Collectors.toList()))
                .points(calculatePoints(processReceiptRequest))
                .build();
    }

    /**
     *
     * Calculates points for the given receipt.
     * <p>
     * One point for every alphanumeric character in the retailer name.
     * 50 points if the total is a round dollar amount with no cents.
     * 25 points if the total is a multiple of 0.25.
     * 5 points for every two items on the receipt.
     * If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned.
     * 6 points if the day in the purchase date is odd.
     * 10 points if the time of purchase is after 2:00pm and before 4:00pm.
     *
     * @param processReceiptRequest
     * @return points for the receipt
     */
    private long calculatePoints(ProcessReceiptRequest processReceiptRequest) {
        log.info("******************** CALCULATING POINTS *****************");
        long points = 0;
        points += calculatePointsInRetailerName(processReceiptRequest);
        points += calculatePointsForRoundTotal(processReceiptRequest);
        points += calculatePointsForQuarterTotal(processReceiptRequest);
        points += calculatePointsForItemCount(processReceiptRequest);
        points += calculatePointsForItemDescription(processReceiptRequest);
        points += calculatePointsForPurchaseDay(processReceiptRequest);
        points += calculatePurchaseTime(processReceiptRequest);

        return points;
    }

    private long calculatePurchaseTime(ProcessReceiptRequest processReceiptRequest) {
        int points = 0;
        LocalTime purchaseTime = processReceiptRequest.getPurchaseTime();
        if (purchaseTime.isAfter(LocalTime.of(14, 0, 0)) && purchaseTime.isBefore(LocalTime.of(16, 0, 0))) {
            points = 10;
            log.info("10 points - {} is between 2:00pm and 4:00pm", purchaseTime);
        }

        return points;
    }

    private long calculatePointsForPurchaseDay(ProcessReceiptRequest processReceiptRequest) {
        int points = 0;
        int dayOfMonth = processReceiptRequest.getPurchaseDate().getDayOfMonth();
        if (dayOfMonth % 2 == 1) {
            points = 6;
            log.info("6 Points - day in the purchase date is odd");
        }
        return points;
    }

    private long calculatePointsForItemDescription(ProcessReceiptRequest processReceiptRequest) {
        long points = 0L;

        for (ReceiptItem receiptItem : processReceiptRequest.getItems()) {
            if (receiptItem.getShortDescription().trim().length() % 3 == 0) {
                points += (long) Math.ceil(Double.parseDouble(receiptItem.getPrice()) * 0.2);
                log.info("3 Points - \"{}\" is {} characters (a multiple of 3)", receiptItem.getShortDescription().trim(), receiptItem.getShortDescription().trim().length());
                log.info("           item price of {} * 0.2 = {}, rounded up is {} points", receiptItem.getPrice(), Double.parseDouble(receiptItem.getPrice()) * 0.2, Math.ceil(Double.parseDouble(receiptItem.getPrice()) * 0.2));
            }
        }



        return points;
    }

    private long calculatePointsForItemCount(ProcessReceiptRequest processReceiptRequest) {
        long points = (processReceiptRequest.getItems().size() / 2) * 5L;

        log.info("{} points - {} items (X pairs @ 5 points each)", points, processReceiptRequest.getItems().size());
        return points;
    }

    private long calculatePointsForQuarterTotal(ProcessReceiptRequest processReceiptRequest) {
        long points = 0L;
        double cost = Double.parseDouble(processReceiptRequest.getTotal());
        if (cost % .25 == 0) {
            points = 25;
            log.info("25 points - total is a multiple of 0.25");
        }
        return points;
    }

    private long calculatePointsForRoundTotal(ProcessReceiptRequest processReceiptRequest) {
        int points = 0;
        double cost = Double.parseDouble(processReceiptRequest.getTotal());
        if (cost % 1 == 0) {
            points = 50;
            log.info("{} points - total is a round dollar amount", points);
        }

        return points;
    }

    private long calculatePointsInRetailerName(ProcessReceiptRequest processReceiptRequest) {
        String retailerName = processReceiptRequest.getRetailer();

        long points = Arrays.stream(retailerName.split(""))
                .filter(StringUtils::isAlphanumeric)
                .count();

        log.info("{} points - retailer name has {} characters", points, points);
        return points;
    }
}
