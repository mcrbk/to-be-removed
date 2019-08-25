package com.money.dao;

import com.money.model.Transfer;
import com.money.model.TransferStatus;

public interface TransferDAO {

    void saveTransfer(Transfer transfer, TransferStatus insufficient);
}
