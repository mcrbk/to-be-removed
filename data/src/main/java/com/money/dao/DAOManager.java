package com.money.dao;

public interface DAOManager {

    AccountDAO accountDAO();

    TransferDAO transferDAO();

    void execute(DAOCommand command);
}
