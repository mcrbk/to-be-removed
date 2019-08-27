package com.money.controller;

import com.money.model.Transfer;
import com.money.model.TransferStatus;
import com.money.service.TransferService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ValidationException;
import java.math.BigDecimal;

@Singleton
@Controller("/api/v1")
public class TransferController {

    private TransferService transferService;

    @Inject
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Post("/transfer/{from}/{to}/{amount}")
    public HttpResponse<Void> transfer(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable String amount) {

        Transfer transfer;

        try {
            transfer = Transfer.builder()
                    .setFrom(from)
                    .setTo(to)
                    .setAmount(new BigDecimal(amount))
                    .setStatus(TransferStatus.UNDEFINED)
                    .build();
        } catch (RuntimeException ex) {
            throw new ValidationException();
        }

        transferService.transfer(transfer);

        return HttpResponse.accepted();
    }
}
