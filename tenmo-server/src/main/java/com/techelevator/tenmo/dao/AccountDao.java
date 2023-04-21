package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccount(int userId);

    BigDecimal getBalanceById(int userId);

    BigDecimal getBalanceByUser(User thisUser);

    void adjustBalance(Account account);

}
