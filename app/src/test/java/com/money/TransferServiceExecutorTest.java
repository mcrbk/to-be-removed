package com.money;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.ExecutorService;

public class TransferServiceExecutorTest {

    private TransferServiceExecutor transferServiceExecutor;

    @BeforeEach
    public void setUp() {
        transferServiceExecutor = new TransferServiceExecutor(() -> null, Mockito.mock(ExecutorService.class));
    }

    @Test
    public void throwsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> transferServiceExecutor.transfer(null));
    }
}
