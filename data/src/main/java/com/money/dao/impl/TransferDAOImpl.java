package com.money.dao.impl;

import com.money.dao.TransferDAO;
import com.money.model.Transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransferDAOImpl implements TransferDAO {

    private static final String SAVE_TRANSFER = "insert into TRANSFER values (?,?,?,?)";

    private Connection connection;

    public TransferDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveTransfer(Transfer transfer) {
        try {
            PreparedStatement ps = connection.prepareStatement(SAVE_TRANSFER);

            ps.setString(1, transfer.from());
            ps.setString(2, transfer.to());
            ps.setString(3, transfer.amount().toPlainString());
            ps.setString(4, transfer.status().name());

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
