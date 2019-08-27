package com.money.dao.impl;

import com.money.dao.DAOTest;
import com.money.dao.TransferDAO;
import com.money.model.Transfer;
import com.money.model.TransferStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferDAOImplTest extends DAOTest {

    private static final String SELECT_FROM_TRANSFER = "select * from TRANSFER";

    private TransferDAO transferDAO;
    private Connection connection;

    @BeforeEach
    public void prepare() throws SQLException {
        transferDAO = new TransferDAOImpl(connection = jdbcDataSource.getConnection());
    }

    @Test
    public void testSaveTransfer() throws SQLException {
        Transfer expected = Transfer.builder()
                .setFrom("from")
                .setTo("to")
                .setAmount(new BigDecimal("100"))
                .setStatus(TransferStatus.SUCCESS)
                .build();

        transferDAO.saveTransfer(expected);

        ResultSet resultSet = connection.prepareStatement(SELECT_FROM_TRANSFER).executeQuery();

        resultSet.next();

        Transfer actual = Transfer.builder()
                .setFrom(resultSet.getString(1))
                .setTo(resultSet.getString(2))
                .setAmount(new BigDecimal(resultSet.getString(3)))
                .setStatus(TransferStatus.valueOf(resultSet.getString(4)))
                .build();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void throwsSQLException() throws SQLException {
        Connection connectionMock;

        transferDAO = new TransferDAOImpl(connectionMock = Mockito.mock(Connection.class));

        Mockito.doThrow(SQLException.class).when(connectionMock).prepareStatement(ArgumentMatchers.anyString());

        Assertions.assertThrows(RuntimeException.class, () -> transferDAO.saveTransfer(Mockito.mock(Transfer.class)));
    }
}
