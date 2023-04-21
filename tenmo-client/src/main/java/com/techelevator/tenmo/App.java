package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLOutput;
import java.util.SortedMap;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    TransferService transferService = new TransferService();
    private final TenmoService tenmoService = new TenmoService(transferService);


    private AuthenticatedUser currentUser;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
//        transferService.setAccountFrom(currentUser.getUser());
    }


    private void mainMenu() {
        User loggedInUser = currentUser.getUser();
        tenmoService.setAccountFrom(loggedInUser);
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                BigDecimal balance = transferService.checkBalance(loggedInUser.getId());
                System.out.println("Your current balance is: $" + balance);
            } else if (menuSelection == 2) {
                tenmoService.getUserTransferList(loggedInUser.getId());
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                selectAccount(menuSelection,loggedInUser);
                sendBucks(menuSelection, loggedInUser);

            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private int getPrincipalId(Principal principal) {
        return transferService.getUserIdByName(principal.getName());
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks(int menuSelection, User loggedInUser) {
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter a value: ");
        System.out.println("Is this the correct amount: $" + amount + "?");
        menuSelection = consoleService.promptForMenuSelection("Please type 1 for Yes or 2 for No: ");
        if (menuSelection == 1) {
            tenmoService.makeTransfer(loggedInUser, tenmoService.getAccountTo(), amount);
        } if (menuSelection == 2) {
           return;
        }
    }

    private void selectAccount(int menuSelection, User loggedInUser) {
        tenmoService.printUserList();
        menuSelection = consoleService.promptForMenuSelection("Please select a user by UserId from the list: ");
        User accountTo = transferService.getUserById(menuSelection);
        if (accountTo.getId() == loggedInUser.getId()) {
            System.out.println("Cannot transfer TE-bucks to your own account.");
            mainMenu();
        }
        tenmoService.setAccountTo(accountTo);
    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

}
