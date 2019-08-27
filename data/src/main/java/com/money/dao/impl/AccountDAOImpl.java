package com.money.dao.impl;

import com.money.dao.AccountDAO;
import com.money.model.Account;
import com.money.model.BalanceUpdate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AccountDAOImpl implements AccountDAO {

    private static final String GET_ACCOUNTS = "select ACCOUNT_NUMBER,BALANCE from ACCOUNT where ACCOUNT_NUMBER in ";
    private static final String UPDATE_BALANCE = "update ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER = ? and BALANCE = ?";

    private Connection connection;

    public AccountDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<Account> getAccounts(String... accounts) {
        if (accounts.length == 1 && accounts[0] == null) {
            throw new NullPointerException();
        }

        Collection<Account> result = new ArrayList<>();

        try {
            StringJoiner parameters = new StringJoiner(",");
            Stream.of(accounts).forEach(s -> parameters.add("?"));

            PreparedStatement ps = connection.prepareStatement(GET_ACCOUNTS + "(" + parameters.toString() + ")");

            int index = 1;
            for (String account : accounts) {
                ps.setString(index++, account);
            }

            ResultSet set = ps.executeQuery();
            while (set.next()) {
                result.add(Account.from(set.getString(1), new BigDecimal(set.getString(2))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int updateBalances(BalanceUpdate... balanceUpdates) {
        if (balanceUpdates.length == 1 && balanceUpdates[0] == null) {
            throw new NullPointerException();
        }

        int[] updateCount;
        try {
            PreparedStatement ps = connection.prepareStatement(UPDATE_BALANCE);

            for (BalanceUpdate balanceUpdate : balanceUpdates) {
                ps.setString(1, balanceUpdate.newBalance().toPlainString());
                ps.setString(2, balanceUpdate.number());
                ps.setString(3, balanceUpdate.oldBalance().toPlainString());
                ps.addBatch();
            }
            updateCount = ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return IntStream.of(updateCount).sum();
    }
}
