package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class TenmoController {

    private AccountService accountService;
    private TransferService transferService;
    private UserService userService;

    public TenmoController(AccountService accountService, TransferService transferService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
        this.transferService = transferService;
    }

    @GetMapping("/tenmo_user")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/tenmo_user/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/tenmo_user/{userId}/account")
    public Account getAccount(@PathVariable Integer userId) {
        return accountService.getAccount(userId);
    }

    @GetMapping("{username}")
    public User getByUsername(@PathVariable String username) {
        return userService.getUserByName(username);
    }

    @GetMapping("account/{userId}/balance")
    public BigDecimal getBalance(@PathVariable Integer userId) {
        return accountService.getBalance(userId);
    }

    @PostMapping("/transfer")
    public Transfer createTransfer(@RequestBody Transfer newTransfer) {
        return transferService.createNewTransfer(newTransfer);
    }

    @GetMapping("/tenmo_user/{username}/id")
    public int getIdByUsername(@PathVariable String username) {
        return userService.getUserIdByName(username);
    }

    @PutMapping("account/{userId}/balance")
    public void adjustBalance(@RequestBody Account account, @PathVariable int userId ) {
        userId = account.getUserId();
        accountService.adjustBalance(account);
    }

    @GetMapping("/tenmo_user/{userId}/transfer")
    public List<Transfer> getPastTransferById(@PathVariable int userId) {
        return transferService.getPastTransfers(userId);
    }

    @GetMapping("/account/{accountId}/user/userId")
    public String getUserNameByAccountId(@PathVariable int accountId) {
        return userService.getUserNameByAccountId(accountId);
    }

}
