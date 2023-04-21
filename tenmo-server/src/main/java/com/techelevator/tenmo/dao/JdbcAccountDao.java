package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Account getAccount(int userId) {
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
        return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getBalanceById(int userId) {

        String sql = "SELECT balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
             return results.getBigDecimal("balance");
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getBalanceByUser(User thisUser) {
        String sql = "SELECT balance FROM account a\n" +
                "JOIN tenmo_user tu ON tu.user_id = a.user_id\n" +
                "WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, thisUser);
        if (results.next()) {
            return results.getBigDecimal("balance");
        } else {
            return null;
        }
    }

    @Override
    public void adjustBalance(Account account) {
        int accountId = account.getAccountId();
        int userId = account.getUserId();
        BigDecimal balance = account.getBalance();

        String sql = "UPDATE account SET account_id = ?, user_id = ?, balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, accountId, userId, balance, userId);
    }

//    @Override
//    public void adjustBalance(BigDecimal amount, User thisUser) {
//        BigDecimal userBalance = getBalanceById(thisUser.getId());
//        int accountId = getAccount(thisUser.getId()).getAccountId();
//        String sql = "UPDATE account SET (account_id = ?, user_id = ?, balance = ?) WHERE user_id = ?";
//        jdbcTemplate.update(sql, accountId, thisUser.getId(), (getBalanceByUser(thisUser).subtract(amount)), thisUser.getId());
//    }


    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();

        account.setAccountId(results.getInt("account_id"));
        account.setBalance(results.getBigDecimal("balance"));
        account.setUserId(results.getInt("user_id"));

        return account;
    }

}
