package com.money.dao;

@FunctionalInterface
public interface DAOCommand<T> {

    T execute();
}
