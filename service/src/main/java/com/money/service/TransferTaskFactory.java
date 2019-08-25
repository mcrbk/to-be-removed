package com.money.service;

import com.money.dao.DAOManager;
import com.money.model.Transfer;

@FunctionalInterface
public interface TransferTaskFactory {

    TransferTask get(Transfer task, DAOManager manager);
}
