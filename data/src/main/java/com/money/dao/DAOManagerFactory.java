package com.money.dao;

@FunctionalInterface
public interface DAOManagerFactory {

    DAOManager get();
}
