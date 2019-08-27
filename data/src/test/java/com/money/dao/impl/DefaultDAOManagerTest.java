package com.money.dao.impl;

import com.money.dao.DAOManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultDAOManagerTest {

    private DataSource dataSource;

    private DAOManager daoManager;

    @BeforeEach
    public void setUp() {
        daoManager = new DefaultDAOManager(dataSource = Mockito.mock(DataSource.class));
    }

    @Test
    public void testExecute() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        Mockito.when(dataSource.getConnection()).thenReturn(connection);

        daoManager.execute(() -> {
        });

        Mockito.verify(connection).setAutoCommit(ArgumentMatchers.eq(false));
        Mockito.verify(connection).commit();
        Mockito.verify(connection).close();
    }

    @Test
    public void testRollback() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        Mockito.when(dataSource.getConnection()).thenReturn(connection);

        Assertions.assertThrows(RuntimeException.class, () ->
                daoManager.execute(() -> {
                    throw new NullPointerException();
                })
        );

        Mockito.verify(connection).setAutoCommit(ArgumentMatchers.eq(false));
        Mockito.verify(connection).rollback();
        Mockito.verify(connection).close();
    }
}