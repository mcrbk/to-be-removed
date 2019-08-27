package com.money;

import com.money.dao.DAOManager;
import com.money.model.Transfer;
import com.money.service.impl.TransferServiceImpl;

public final class TransferServiceTransactional extends TransferServiceImpl {

    private final DAOManager daoManager;

    public TransferServiceTransactional(DAOManager daoManager) {
        super(daoManager.accountDAO(), daoManager.transferDAO());
        this.daoManager = daoManager;
    }

    @Override
    public void transfer(Transfer transfer) {
        daoManager.execute(() -> super.transfer(transfer));
    }
}
