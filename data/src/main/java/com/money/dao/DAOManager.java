package com.money.dao;

public interface DAOManager {

    AccountDAO accountDAO();

    TransferDAO transferDAO();

    <T> T execute(DAOCommand<T> command);

    <T> T transaction(DAOCommand<T> command);
}
