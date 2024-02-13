package dev.dkramer.fetch.challenge.api.models;

import lombok.NonNull;
import lombok.Value;

/**
 *  schema:
 *      type: object
 *      required:
 *          - shortDescription
 *          - price
 *      properties:
 *          shortDescription:
 *              description: The Short Product Description for the item.
 *              type: string
 *              pattern: "^[\\w\\s\\-]+$"
 *              example: "Mountain Dew 12PK"
 *          price:
 *              description: The total price payed for this item.
 *              type: string
 *              pattern: "^\\d+\\.\\d{2}$"
 *              example: "6.49"
 */
@Value
public class ReceiptItem {
    @NonNull
    private final String shortDescription;

    @NonNull
    private final String price;

}
