package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TransferService {

    private User accountFrom;
    private User accountTo;
    private BigDecimal amount;
    private String baseUrl = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private Account account;


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAccountTo(User accountTo) {
        this.accountTo = accountTo;
    }

    public void setAccountFrom(User accountFrom) {
        this.accountFrom = accountFrom;
    }



    public BigDecimal checkBalance(int userId) {
        BigDecimal balance = restTemplate.getForObject(baseUrl + "account/" + userId + "/balance", BigDecimal.class);
        return balance;
    }

    public Integer getUserIdByName(String userName) {
        return restTemplate.getForObject(baseUrl + "tenmo_user/" + userName + "/id", Integer.class);
    }

    public User getUserByName(String userName) {
        return restTemplate.getForObject(baseUrl + userName, User.class);
    }

    public User getUserById(int userId) {
        User user = restTemplate.getForObject(baseUrl + "tenmo_user/" + userId, User.class);
        return user;
    }

    public Account getUserAccountById(int userId) {
        return restTemplate.getForObject(baseUrl + "tenmo_user/" + userId + "/account", Account.class);
    }

    public void updateBalance(Account account) {
        int userId = account.getUserId();
        HttpEntity<Account> entity = makeAccountEntity(account);

        try {
            restTemplate.put(baseUrl + "account/" + userId + "/balance",
                    entity);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }


    public void createTransfer(Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);

        try {
            restTemplate.postForObject(baseUrl + "transfer", entity, Transfer.class);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getUserNameByAccountId(int accountId) {
        return restTemplate.getForObject(baseUrl + "account/" + accountId + "/user/userId", String.class);
    }


    private HttpEntity<Account> makeAccountEntity (Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Transfer> makeTransferEntity (Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(transfer, headers);
    }

}
