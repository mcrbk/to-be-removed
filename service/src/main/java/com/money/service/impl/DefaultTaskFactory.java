package com.money.service.impl;

import com.money.dao.DAOManager;
import com.money.model.Transfer;
import com.money.service.TransferTask;
import com.money.service.TransferTaskFactory;

public class DefaultTaskFactory implements TransferTaskFactory {

    @Override
    public TransferTask get(Transfer transfer, DAOManager manager) {
        return new TransferTaskImpl(transfer, manager);
    }
}
