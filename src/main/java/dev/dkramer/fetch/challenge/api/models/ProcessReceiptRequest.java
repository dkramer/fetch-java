package dev.dkramer.fetch.challenge.api.models;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *  schema:
 *      retailer:
 *          description: The name of the retailer or store the receipt is from.
 *          type: string
 *          pattern: "^[\\w\\s\\-]+$"
 *          example: "M&M Corner Market"
 *      purchaseDate:
 *          description: The date of the purchase printed on the receipt.
 *          type: string
 *          format: date
 *          example: "2022-01-01"
 *      purchaseTime:
 *          description: The time of the purchase printed on the receipt. 24-hour time expected.
 *          type: string
 *          format: time
 *          example: "13:01"
 *      items:
 *          type: array
 *          minItems: 1
 *          items:
 *              $ref: "#/components/schemas/Item"
 *      total:
 *          description: The total amount paid on the receipt.
 *          type: string
 *          pattern: "^\\d+\\.\\d{2}$"
 *          example: "6.49"
 */
@Value
public class ProcessReceiptRequest {

    @NonNull
    private final String retailer;

    @NonNull
    private final LocalDate purchaseDate;

    @NonNull
    private final LocalTime purchaseTime;

    @NonNull
    private final List<ReceiptItem> items;

    @NonNull
    private final String total;

}
