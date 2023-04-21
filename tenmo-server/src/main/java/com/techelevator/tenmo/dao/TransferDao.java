package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getUserTransfers(int userId);

    Transfer getTransfer(int transferId);

    Transfer createTransfer(Transfer newTransfer);


}
