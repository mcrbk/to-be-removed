package com.money.service.impl;

import com.money.dao.AccountDAO;
import com.money.dao.TransferDAO;
import com.money.model.Account;
import com.money.model.BalanceUpdate;
import com.money.model.Transfer;
import com.money.model.TransferStatus;
import com.money.service.TransferService;
import com.money.service.TransferUpdateException;

import java.util.Collection;
import java.util.Objects;

public class TransferServiceImpl implements TransferService {

    private AccountDAO accountDAO;
    private TransferDAO transferDAO;

    public TransferServiceImpl(AccountDAO accountDAO, TransferDAO transferDAO) {
        this.accountDAO = accountDAO;
        this.transferDAO = transferDAO;
    }

    @Override
    public void transfer(Transfer transfer) {
        Objects.requireNonNull(transfer);

        var accounts = accountDAO.getAccounts(transfer.from(), transfer.to());

        var accountFrom = getAccount(accounts, transfer.from());
        var accountTo = getAccount(accounts, transfer.to());

        if (!isValid(accountFrom, accountTo)) {
            transferDAO.saveTransfer(transfer.toBuilder().setStatus(TransferStatus.VALIDATION_FAILURE).build());
            System.out.println("VALIDATION FAILURE");
            return;
        }

        if (!isSufficient(transfer, accountFrom)) {
            transferDAO.saveTransfer(transfer.toBuilder().setStatus(TransferStatus.INSUFFICIENT_FUNDS).build());
            System.out.println("INSUFFICIENT FUNDS");
            return;
        }

        var fromNewBalance = BalanceUpdate.builder()
                .setNumber(accountFrom.number())
                .setNewBalance(accountFrom.balance().subtract(transfer.amount()))
                .setOldBalance(accountFrom.balance())
                .build();

        var toNewBalance = BalanceUpdate.builder()
                .setNumber(accountTo.number())
                .setNewBalance(accountTo.balance().add(transfer.amount()))
                .setOldBalance(accountTo.balance())
                .build();

        int count = accountDAO.updateBalances(fromNewBalance, toNewBalance);

        if (count < 2) {
            throw new TransferUpdateException("An operation failed due to stale data");
        }

        transferDAO.saveTransfer(transfer.toBuilder().setStatus(TransferStatus.SUCCESS).build());
        System.out.println("SUCCESS");
    }

    /*
     * Validation before each transfer attempt
     * It would be more complex logic to validate accounts, associated customers and so on
     * This has been omitted for simplicity
     * */
    private boolean isValid(Account from, Account to) {
        return Objects.nonNull(from) && Objects.nonNull(to);
    }

    private boolean isSufficient(Transfer transfer, Account accountFrom) {
        return accountFrom.balance().subtract(transfer.amount()).signum() >= 0;
    }

    private Account getAccount(Collection<Account> accounts, String from) {
        return accounts.stream().filter(account -> account.number().equals(from)).findAny().orElse(null);
    }
}
