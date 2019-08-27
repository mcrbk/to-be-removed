package com.money.dao.impl;

import com.money.dao.AccountDAO;
import com.money.dao.TransferDAO;

import javax.sql.DataSource;

/*
 * Non-thread safe
 * */
public class DefaultDAOManager extends AbstractDAOManager {

    public DefaultDAOManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public AccountDAO accountDAO() {
        return new AccountDAOImpl(connection());
    }

    @Override
    public TransferDAO transferDAO() {
        return new TransferDAOImpl(connection());
    }
}
