package dev.dkramer.fetch.challenge.api.models;

import lombok.Value;

/**
 *  schema:
 *      type: object
 *      required:
 *          - id
 *      properties:
 *          id:
 *              type: string
 *              pattern: "^\\S+$"
 *              example: adb6b560-0eef-42bc-9d16-df48f30e89b2
 */
@Value
public class ProcessReceiptResponse {
    private final String id;
}
