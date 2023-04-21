package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountService {
    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao){
            this.accountDao = accountDao;
    }

    public Account getAccount(int userId) {
        return accountDao.getAccount(userId);
    }

    public BigDecimal getBalance(int accountId) {
        return accountDao.getBalanceById(accountId);
    }

    public void adjustBalance(Account account) {
        accountDao.adjustBalance(account);
    }


}
