package dev.dkramer.fetch.challenge.api;

import dev.dkramer.fetch.challenge.api.models.GetReceiptPointsResponse;
import dev.dkramer.fetch.challenge.api.models.ProcessReceiptRequest;
import dev.dkramer.fetch.challenge.api.models.ProcessReceiptResponse;
import dev.dkramer.fetch.challenge.service.GetReceiptPointsService;
import dev.dkramer.fetch.challenge.service.ProcessReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Resource class that handles /receipt calls.
 */
@RestController
@RequestMapping("/receipts")
public class ReceiptResource {

    @Autowired
    ProcessReceiptService processReceiptService;

    @Autowired
    GetReceiptPointsService getReceiptPointsService;


    @PostMapping("/process")
    @ResponseStatus(HttpStatus.OK)
    public ProcessReceiptResponse processReceipt(@RequestBody ProcessReceiptRequest processReceiptRequest) {

        return new ProcessReceiptResponse(processReceiptService.processReceipt(processReceiptRequest));
    }


    @GetMapping("/{id}/points")
    @ResponseStatus(HttpStatus.OK)
    public GetReceiptPointsResponse getReceiptPoints(@PathVariable("id") String id) {

        return new GetReceiptPointsResponse(getReceiptPointsService.getReceiptPoints(id));
    }
}
