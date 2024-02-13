package dev.dkramer.fetch.challenge.service;

import dev.dkramer.fetch.challenge.repository.ReceiptRepository;
import dev.dkramer.fetch.challenge.repository.models.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class GetReceiptPointsService {

    @Autowired
    private ReceiptRepository receiptRepository;

    public long getReceiptPoints(String id) {
        Optional<Receipt> receipt = receiptRepository.getReceiptById(id);

        return receipt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receipt not found.")).getPoints();
    }
}
