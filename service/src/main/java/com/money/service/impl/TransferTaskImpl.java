package com.money.service.impl;

import com.money.dao.DAOManager;
import com.money.model.Account;
import com.money.model.BalanceUpdate;
import com.money.model.Transfer;
import com.money.model.TransferStatus;
import com.money.service.TransferTask;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Objects;

public class TransferTaskImpl implements TransferTask {

    private Transfer transfer;
    private DAOManager manager;

    public TransferTaskImpl(Transfer transfer, DAOManager manager) {
        this.transfer = Objects.requireNonNull(transfer);
        this.manager = Objects.requireNonNull(manager);
    }

    @Override
    public void run() {
        var accounts = manager.execute(() -> manager.accountDAO().getAccounts(transfer.from(), transfer.to()));

        var accountFrom = getAccount(accounts, transfer.from());
        var accountTo = getAccount(accounts, transfer.to());

        if (!isValid(accountFrom, accountTo)) {
            saveTransferWithStatus(TransferStatus.VALIDATION_FAILURE);
            return;
        }

        if (!isSufficient(accountFrom)) {
            saveTransferWithStatus(TransferStatus.INSUFFICIENT_FUNDS);
            return;
        }

        var fromNewBalance = BalanceUpdate.of(accountFrom, accountFrom.balance().subtract(transfer.amount()), accountFrom.balance());
        var toNewBalance = BalanceUpdate.of(accountTo, accountTo.balance().add(transfer.amount()), accountTo.balance());

        manager.transaction(() -> {
            int count = manager.accountDAO().updateBalances(fromNewBalance, toNewBalance);

            if (count < 2) {
                throw new ConcurrentModificationException("Transaction failed due to stale data");
            }

            manager.transferDAO().saveTransfer(transfer, TransferStatus.SUCCESS);
            return null;
        });
    }

    private void saveTransferWithStatus(TransferStatus status) {
        manager.execute(() -> {
            manager.transferDAO().saveTransfer(transfer, status);
            return null;
        });
    }

    /*
     * Validation before each transfer attempt
     * It would be more complex logic to validate accounts, associated customers and so on
     * This has been omitted for simplicity
     * */
    private boolean isValid(Account from, Account to) {
        return Objects.nonNull(from) && Objects.nonNull(to);
    }

    private boolean isSufficient(Account accountFrom) {
        return accountFrom.balance().subtract(transfer.amount()).signum() > 0;
    }

    private Account getAccount(Collection<Account> accounts, String from) {
        return accounts.stream().filter(account -> account.number().equals(from)).findAny().orElse(null);
    }
}
