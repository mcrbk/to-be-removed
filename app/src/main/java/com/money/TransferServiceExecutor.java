package com.money;

import com.money.dao.DAOManager;
import com.money.model.Transfer;
import com.money.service.TransferService;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/*
 *  Delegate a transfer task to an executor
 *  It would be ideally a message broker like RabbitMQ with consumers
 * */
public class TransferServiceExecutor implements TransferService {

    private final ExecutorService executorService;

    private final Supplier<DAOManager> managerFactory;

    public TransferServiceExecutor(Supplier<DAOManager> managerFactory, ExecutorService executorService) {
        this.managerFactory = managerFactory;
        this.executorService = executorService;
    }

    @Override
    public void transfer(Transfer transfer) {
        Objects.requireNonNull(transfer, "Transfer cannot be null");

        executorService.submit(() -> new TransferServiceTransactional(managerFactory.get()).transfer(transfer));
    }
}
