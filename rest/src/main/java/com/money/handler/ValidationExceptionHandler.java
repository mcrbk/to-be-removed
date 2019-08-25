package com.money.handler;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;
import javax.validation.ValidationException;

@Singleton
public class ValidationExceptionHandler implements ExceptionHandler<ValidationException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, ValidationException exception) {
        return HttpResponse.badRequest();
    }
}
