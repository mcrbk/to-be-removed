package com.money.dao.impl;

import com.money.dao.DAOCommand;
import com.money.dao.DAOManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAOManager implements DAOManager {

    private DataSource dataSource;
    private Connection connection;

    protected AbstractDAOManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void execute(DAOCommand command) {
        try {
            connection().setAutoCommit(false);
            command.execute();
            connection().commit();
        } catch (Exception e) {
            rollback();
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    protected Connection connection() {
        try {
            return connection = connection == null ? dataSource.getConnection() : connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void rollback() {
        try {
            connection().rollback();
        } catch (SQLException e) {
            //Ignore any exception, log here if necessary
        }
    }

    private void closeConnection() {
        try {
            connection().close();
        } catch (SQLException e) {
            //Ignore any exception, log here if necessary
        }
    }
}
