package com.money.handler;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Singleton
public class DefaultExceptionHandler implements ExceptionHandler<Exception, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, Exception exception) {
        return HttpResponse.serverError();
    }
}
