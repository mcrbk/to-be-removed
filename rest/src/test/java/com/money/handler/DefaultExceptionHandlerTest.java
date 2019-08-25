package com.money.handler;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DefaultExceptionHandlerTest {

    @Test
    void testResponse() {
        DefaultExceptionHandler exceptionHandler = new DefaultExceptionHandler();

        HttpResponse expected = exceptionHandler.handle(Mockito.mock(HttpRequest.class), Mockito.mock(Exception.class));

        Assertions.assertEquals(expected.getStatus(), HttpResponse.serverError().getStatus());
    }
}