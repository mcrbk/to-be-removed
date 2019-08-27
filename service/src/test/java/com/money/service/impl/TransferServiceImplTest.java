package com.money.service.impl;

import com.money.dao.AccountDAO;
import com.money.dao.TransferDAO;
import com.money.model.Account;
import com.money.model.BalanceUpdate;
import com.money.model.Transfer;
import com.money.model.TransferStatus;
import com.money.service.TransferService;
import com.money.service.TransferUpdateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

public class TransferServiceImplTest {

    private static final Transfer TRANSFER = Transfer.builder()
            .setFrom("from")
            .setTo("to")
            .setAmount(new BigDecimal("100"))
            .setStatus(TransferStatus.UNDEFINED)
            .build();

    private AccountDAO accountDAO;
    private TransferDAO transferDAO;

    private TransferService transferService;

    @BeforeEach
    public void setUp() {
        transferService = new TransferServiceImpl(
                accountDAO = Mockito.mock(AccountDAO.class),
                transferDAO = Mockito.mock(TransferDAO.class)
        );
    }

    @Test
    public void testTransfer() {
        teach("200");

        transferService.transfer(TRANSFER);

        Mockito.verify(transferDAO).saveTransfer(TRANSFER.toBuilder().setStatus(TransferStatus.SUCCESS).build());
    }

    @Test
    public void throwsTransferUpdateException() {
        teach("100");

        Assertions.assertThrows(TransferUpdateException.class, () -> transferService.transfer(TRANSFER));
    }

    @Test
    public void testValidationFailure() {
        Mockito.when(accountDAO.getAccounts(ArgumentMatchers.eq("from"), ArgumentMatchers.eq("to"))).thenReturn(Collections.emptyList());

        transferService.transfer(TRANSFER);

        Mockito.verify(transferDAO).saveTransfer(TRANSFER.toBuilder().setStatus(TransferStatus.VALIDATION_FAILURE).build());
    }

    @Test
    public void testInsufficientFunds() {
        Mockito.when(accountDAO.getAccounts(ArgumentMatchers.eq("from"), ArgumentMatchers.eq("to"))).thenReturn( Arrays.asList(
                Account.from("from", new BigDecimal("50")),
                Account.from("to", new BigDecimal("100"))
        ));

        transferService.transfer(TRANSFER);

        Mockito.verify(transferDAO).saveTransfer(TRANSFER.toBuilder().setStatus(TransferStatus.INSUFFICIENT_FUNDS).build());
    }

    @Test
    public void throwsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> transferService.transfer(null));
    }

    private void teach(String newBalanceAmount) {
        Mockito.when(accountDAO.getAccounts(ArgumentMatchers.eq("from"), ArgumentMatchers.eq("to"))).thenReturn(Arrays.asList(
                Account.from("from", new BigDecimal("100")),
                Account.from("to", new BigDecimal("100"))
        ));

        BalanceUpdate[] balances = new BalanceUpdate[]{
                ArgumentMatchers.eq(BalanceUpdate.builder().setNumber("from").setNewBalance(new BigDecimal("0")).setOldBalance(new BigDecimal("100")).build()),
                ArgumentMatchers.eq(BalanceUpdate.builder().setNumber("to").setNewBalance(new BigDecimal(newBalanceAmount)).setOldBalance(new BigDecimal("100")).build())
        };

        Mockito.when(accountDAO.updateBalances(balances)).thenReturn(2);
    }
}