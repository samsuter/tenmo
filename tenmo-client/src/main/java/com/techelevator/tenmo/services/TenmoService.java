package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TenmoService {

    private String baseUrl = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private TransferService transferService = new TransferService();

    private User accountFrom;
    private User accountTo;
    private BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAccountTo(User accountTo) {
        this.accountTo = accountTo;
    }

    public void setAccountFrom(User accountFrom) {
        this.accountFrom = accountFrom;
    }

    public User getAccountFrom() {
        return accountFrom;
    }

    public User getAccountTo() {
        return accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TenmoService(TransferService transferService) {
        this.transferService = transferService;
    }


    public void printUserList() {
        User[] userArray = restTemplate.getForObject(baseUrl + "tenmo_user", User[].class);
        for (User curUser : userArray) {
            if (!curUser.equals(accountFrom)) {
                System.out.print(curUser.getId() + ": " + curUser.getUsername());
                System.out.println();
            }
        }
    }

    public void updateBalance(User userTo, BigDecimal amount) {
        int userId = userTo.getId();
        Account account = transferService.getUserAccountById(userId);
        int accountId = account.getAccountId();

        BigDecimal balance = transferService.checkBalance(userId);
        BigDecimal newBalance = balance.add(amount);
        account.setBalance(newBalance);
        account.setUserId(userId);
        account.setAccountId(accountId);

        transferService.updateBalance(account);
    }

    public Transfer makeTransfer(User accountFrom, User accountTo, BigDecimal transferAmount) {
        Transfer newTransfer = new Transfer();
        int userOneId = accountFrom.getId();
        int userTwoId = accountTo.getId();
        if (transferAmount.compareTo(transferService.checkBalance(userOneId)) > 0) {
            System.out.println("Insufficient Funds");
        } else if (userOneId == userTwoId) {
            return null;
        } else {
            updateBalance(accountTo, transferAmount);
            updateBalance(accountFrom, transferAmount.multiply(new BigDecimal(-1)));
            System.out.println("Transfer successful!");
            System.out.println("Your new balance is: " + transferService.checkBalance(userOneId));
            newTransfer.setTransferTypeId(2);
            newTransfer.setTransferStatusId(2);
            newTransfer.setAmount(transferAmount);
            newTransfer.setAccountTo(userTwoId);
            newTransfer.setAccountFrom(userOneId);
            transferService.createTransfer(newTransfer);
        }
        return newTransfer;
    }

    public void getUserTransferList(int userId) {
        Transfer[] userTransfer = restTemplate.getForObject(baseUrl + "/tenmo_user/" + userId + "/transfer",
                Transfer[].class);

        System.out.println("Your Transfers:");
        System.out.println("***********************");
        System.out.println();
        for (Transfer curTransfer : userTransfer) {
            if (curTransfer.getAccountTo() == transferService.getUserAccountById(userId).getAccountId()) {
                System.out.println("Transfer ID: " + curTransfer.getTransferId());
                System.out.println("To: " + transferService.getUserNameByAccountId(curTransfer.getAccountFrom()));
                if (curTransfer.getTransferTypeId() == 1) {
                    System.out.println("Transfer Type: Request");
                } else if (curTransfer.getTransferTypeId() == 2) {
                    System.out.println("Transfer Type: Send");
                }
                System.out.println("Amount: $" + curTransfer.getAmount());
            } else {
                System.out.println("Transfer ID: " + curTransfer.getTransferId());
                System.out.println("From: " + transferService.getUserNameByAccountId(curTransfer.getAccountTo()));
                if (curTransfer.getTransferTypeId() == 1) {
                    System.out.println("Transfer Type: Request");
                } else if (curTransfer.getTransferTypeId() == 2) {
                    System.out.println("Transfer Type: Send");
                }
                System.out.println("Amount: $" + curTransfer.getAmount());
            }
            System.out.println("---------------------------");
        }
    }


}





