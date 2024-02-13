package dev.dkramer.fetch.challenge.api.models;

import lombok.Value;

/**
 *  schema:
 *      type: object
 *      properties:
 *          points:
 *              type: integer
 *              format: int64
 *              example: 100
 */
@Value
public class GetReceiptPointsResponse {
    private final long points;
}
