package com.money.service.impl;

import com.money.dao.DAOManagerFactory;
import com.money.model.Transfer;
import com.money.service.TransferService;
import com.money.service.TransferTaskFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/*
 *  Delegate a transfer task to an executor
 *  It would be ideally a message broker like RabbitMQ with consumers
 * */
public class TransferServiceImpl implements TransferService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10, init(Executors.defaultThreadFactory()));

    private final TransferTaskFactory transferTaskFactory;
    private final DAOManagerFactory managerFactory;

    public TransferServiceImpl(TransferTaskFactory transferTaskFactory, DAOManagerFactory managerFactory) {
        this.transferTaskFactory = transferTaskFactory;
        this.managerFactory = managerFactory;
    }

    @Override
    public void transfer(Transfer transfer) {
        Objects.requireNonNull(transfer, "Transfer cannot be null");

        executorService.submit(transferTaskFactory.get(transfer, managerFactory.get()));
    }

    private ThreadFactory init(ThreadFactory threadFactory) {
        return runnable -> {
            Thread thread = threadFactory.newThread(runnable);
            thread.setUncaughtExceptionHandler((x, t) -> executorService.submit(runnable));
            return thread;
        };
    }
}
