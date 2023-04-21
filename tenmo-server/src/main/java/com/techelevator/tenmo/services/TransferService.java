package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Component
public class TransferService {

    private TransferDao transferDao;
    private AccountService accountService;
    private UserService userService;

    public TransferService(TransferDao transferDao, AccountService accountService, UserService userService){
        this.transferDao = transferDao;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Transactional
    public Transfer createNewTransfer(@Valid Transfer newTransfer) {
        return transferDao.createTransfer(newTransfer);
    }

    public List<Transfer> getPastTransfers (int userId) {
        return transferDao.getUserTransfers(userId);
    }
}
