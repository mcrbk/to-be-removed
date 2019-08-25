package com.money.dao;

import com.money.model.Account;
import com.money.model.BalanceUpdate;

import java.util.Collection;

public interface AccountDAO {

    Collection<Account> getAccounts(String... accounts);

    int updateBalances(BalanceUpdate... balanceUpdates);
}
