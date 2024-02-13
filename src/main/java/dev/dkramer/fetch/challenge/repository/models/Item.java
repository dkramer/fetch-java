package dev.dkramer.fetch.challenge.repository.models;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 *
 */
@Value
@Builder
public class Item {
    @NonNull
    private final String itemId;

    @NonNull
    private final String shortDescription;

    @NonNull
    private final double price;

    @NonNull
    private final String receiptId;

}
