package com.money.dao.impl;

import com.money.dao.AccountDAO;
import com.money.dao.DAOTest;
import com.money.model.Account;
import com.money.model.BalanceUpdate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAOImplTest extends DAOTest {

    private static final String SELECT_FROM_ACCOUNTS = "select * from ACCOUNT";

    private AccountDAO accountDAO;
    private Connection connection;


    @BeforeEach
    public void prepare() throws SQLException {
        accountDAO = new AccountDAOImpl(connection = jdbcDataSource.getConnection());
    }

    @Test
    public void testGetAccounts() throws SQLException {
        populateDatabase();

        Account expected = accountDAO.getAccounts("ABC").iterator().next();

        ResultSet resultSet = connection.prepareStatement(SELECT_FROM_ACCOUNTS).executeQuery();

        resultSet.next();

        Account actual = Account.from(resultSet.getString(1), new BigDecimal(resultSet.getString(2)));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testUpdateBalances() throws SQLException {
        populateDatabase();

        int actual = accountDAO.updateBalances(BalanceUpdate.builder().setNumber("ABC").setNewBalance(new BigDecimal("456")).setOldBalance(new BigDecimal("123")).build());

        Assertions.assertEquals(1, actual);
    }

    @Test
    public void throwsNullPointerException() throws SQLException {
        String accounts = null;
        BalanceUpdate balances = null;

        Assertions.assertThrows(NullPointerException.class, () -> accountDAO.getAccounts(accounts));
        Assertions.assertThrows(NullPointerException.class, () -> accountDAO.updateBalances(balances));
    }

    @Test
    public void throwsSQLException() throws SQLException {
        Connection connectionMock;

        accountDAO = new AccountDAOImpl(connectionMock = Mockito.mock(Connection.class));

        Mockito.doThrow(SQLException.class).when(connectionMock).prepareStatement(ArgumentMatchers.anyString());

        Assertions.assertThrows(RuntimeException.class, () -> accountDAO.getAccounts(""));
        Assertions.assertThrows(RuntimeException.class, () -> accountDAO.updateBalances(Mockito.mock(BalanceUpdate.class)));
    }

    private boolean populateDatabase() throws SQLException {
        return connection.prepareStatement("insert into ACCOUNT values ('ABC','123')").execute();
    }
}
