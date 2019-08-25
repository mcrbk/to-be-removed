package com.money.dao.impl;

import com.money.dao.DAOCommand;
import com.money.dao.DAOManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/*
 * Close connection after each command execution
 *
 * Improvements needed to manage more effectively database connections
 * */
public abstract class AbstractDAOManager implements DAOManager {

    private DataSource dataSource;
    private Connection connection;

    protected AbstractDAOManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T transaction(DAOCommand<T> command) {
        try {
            connection().setAutoCommit(false);
            T result = command.execute();
            connection().commit();
            return result;
        } catch (Exception e) {
            rollback();
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public <T> T execute(DAOCommand<T> command) {
        try {
            return command.execute();
        } catch (Exception e) {
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
