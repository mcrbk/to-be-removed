package com.money.handler;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ValidationException;

public class ValidationExceptionHandlerTest {

    @Test
    void testResponse() {
        ValidationExceptionHandler exceptionHandler = new ValidationExceptionHandler();

        HttpResponse expected = exceptionHandler.handle(Mockito.mock(HttpRequest.class), Mockito.mock(ValidationException.class));

        Assertions.assertEquals(expected.getStatus(), HttpResponse.badRequest().getStatus());
    }
}
