package dev.dkramer.fetch.challenge.repository.models;

import dev.dkramer.fetch.challenge.api.models.ReceiptItem;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Represents a receipt in the DB.
 */
@Value
@Builder
public class Receipt {

    @NonNull
    private final String receiptId;

    @NonNull
    private final String retailer;

    @NonNull
    private final LocalDate purchaseDate;

    @NonNull
    private final LocalTime purchaseTime;

    @NonNull
    private final List<Item> receiptItems;

    @NonNull
    private final double total;

    @NonNull
    private final long points;

}
