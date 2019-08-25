package com.money.controller;

import com.money.model.Transfer;
import com.money.service.TransferService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.inject.Inject;

@MicronautTest
public class TransferControllerTest {

    @Inject
    TransferService transferServiceMock;

    @Inject
    @Client("/api/v1")
    HttpClient httpClient;

    @Test
    public void testTransfer() {
        Assertions.assertEquals(HttpStatus.ACCEPTED, httpClient.toBlocking().exchange(HttpRequest.POST("/transfer/mike/sam/10", "")).status());
    }

    @Test
    public void testTransferWithInvalidAmount() {
        HttpClientResponseException response = Assertions.assertThrows(HttpClientResponseException.class,
                () -> httpClient.toBlocking().exchange(HttpRequest.POST("/transfer/mike/sam/0", "")));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testTransferWithInvalidAccount() {
        HttpClientResponseException response = Assertions.assertThrows(HttpClientResponseException.class,
                () -> httpClient.toBlocking().exchange(HttpRequest.POST("/transfer/mike/%20/10", "")));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testTransferWithUnexpectedError() {
        Mockito.doThrow(NullPointerException.class).when(transferServiceMock).transfer(ArgumentMatchers.any(Transfer.class));

        HttpClientResponseException response = Assertions.assertThrows(HttpClientResponseException.class,
                () -> httpClient.toBlocking().exchange(HttpRequest.POST("/transfer/mike/sam/10", "")));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @MockBean(TransferService.class)
    public TransferService transferService() {
        TransferService transferServiceMock = Mockito.mock(TransferService.class);

        Mockito.doNothing().when(transferServiceMock).transfer(ArgumentMatchers.any(Transfer.class));

        return transferServiceMock;
    }
}
