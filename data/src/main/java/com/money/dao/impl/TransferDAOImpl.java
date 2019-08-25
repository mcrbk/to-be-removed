package com.money.dao.impl;

import com.money.dao.TransferDAO;
import com.money.model.Transfer;
import com.money.model.TransferStatus;

import java.sql.Connection;

public class TransferDAOImpl implements TransferDAO {

    private Connection connection;

    public TransferDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveTransfer(Transfer transfer, TransferStatus insufficient) {

    }
}
