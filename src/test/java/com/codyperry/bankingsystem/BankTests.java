package com.codyperry.bankingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codyperry.bankingsystem.model.Bank;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankTests {

    @Test
    @Order(1)
    void testHappyPathCreation() {
        final Bank bank = new Bank();
        boolean createdAccount = bank.createAccount(0, "account1");
        assertEquals(createdAccount, true);
    }

    @Test
    @Order(2)
    void testCreateDuplicateAccountId() {
        final Bank bank = new Bank();
        boolean firstAccount = bank.createAccount(0, "account1");
        boolean secondAccount = bank.createAccount(0, "account1");

        assertEquals(firstAccount, true);
        assertEquals(secondAccount, false);
    }

    @Test
    @Order(3)
    void testFirstDeposit() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");

        Optional<Integer> balance = bank.deposit(0, "account1", 100);

        assertEquals(balance.isPresent(), true);
        assertEquals(balance.get(), 100);
    }

    @Test
    @Order(4)
    void testSubsequentDeposits() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");

        Optional<Integer> initialBalance = bank.deposit(0, "account1", 100);
        Optional<Integer> currBalance = bank.deposit(0, "account1", 100);

        assertEquals(initialBalance.isPresent(), true);
        assertEquals(initialBalance.get(), 100);

        assertEquals(currBalance.isPresent(), true);
        assertEquals(currBalance.get(), 200);
    }

    @Test
    @Order(5)
    void testDepositNonExistentAccount() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");

        Optional<Integer> initialBalance = bank.deposit(0, "account2", 100);

        assertEquals(initialBalance.isPresent(), false);
        assertEquals(initialBalance.isEmpty(), true);
    }

    @Test
    @Order(6)
    void testPayHappyPath() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");

        bank.deposit(0, "account1", 100);
        Optional<Integer> paid = bank.pay(0, "account1", 50);

        assertEquals(paid.isPresent(), true);
        assertEquals(paid.get(), 50);
    }

    @Test
    @Order(7)
    void testPayNonExistentAccount() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");

        bank.deposit(0, "account1", 100);
        Optional<Integer> paid = bank.pay(0, "account2", 50);

        assertEquals(paid.isPresent(), false);
        assertEquals(paid.isEmpty(), true);
    }

    @Test
    @Order(8)
    void testPayInsufficientFunds() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");

        bank.deposit(0, "account1", 40);
        Optional<Integer> paid = bank.pay(0, "account1", 50);

        assertEquals(paid.isPresent(), false);
        assertEquals(paid.isEmpty(), true);
    }

    @Test
    @Order(9)
    void testCloseHappyPath() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");
        bank.deposit(0, "account1", 40);

        Optional<Integer> remainingBalance = bank.closeAccount(0, "account1");

        assertEquals(remainingBalance.isPresent(), true);
        assertEquals(remainingBalance.get(), 40);
    }

    @Test
    @Order(10)
    void testCloseNonExistentAcct() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");
        bank.deposit(0, "account1", 40);

        Optional<Integer> remainingBalance = bank.closeAccount(0, "account2");

        assertEquals(remainingBalance.isPresent(), false);
        assertEquals(remainingBalance.isEmpty(), true);
    }

    @Test
    @Order(11)
    void testTransferHappyPath() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");
        bank.createAccount(0, "account2");
        bank.deposit(0, "account1", 40);

        Optional<String> transferId = bank.initiateTransfer(0, "account1", "account2", 20);

        assertEquals(transferId.isPresent(), true);

        boolean acceptedTransfer = bank.acceptTransfer(1, "account2", transferId.get());
        assertEquals(acceptedTransfer, true);

        Optional<Integer> account1Balance = bank.getBalance(2, "account1");
        Optional<Integer> account2Balance = bank.getBalance(2, "account2");

        assertEquals(account1Balance.isPresent(), true);
        assertEquals(account1Balance.get(), 20);
        assertEquals(account2Balance.isPresent(), true);
        assertEquals(account1Balance.get(), 20);
    }

    @Test
    @Order(12)
    void testTransferMissingAcct() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");
        bank.deposit(0, "account1", 40);

        Optional<String> transferId = bank.initiateTransfer(0, "account1", "account2", 20);

        assertEquals(transferId.isPresent(), false);
        assertEquals(transferId.isEmpty(), true);
    }

    @Test
    @Order(13)
    void testTransferInsufficientFunds() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");
        bank.createAccount(0, "account2");
        bank.deposit(0, "account1", 15);

        Optional<String> transferId = bank.initiateTransfer(0, "account1", "account2", 20);

        assertEquals(transferId.isPresent(), false);
        assertEquals(transferId.isEmpty(), true);
    }

    @Test
    @Order(14)
    void testTransferAcceptedAlready() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");
        bank.createAccount(0, "account2");
        bank.deposit(0, "account1", 40);

        Optional<String> transferId = bank.initiateTransfer(0, "account1", "account2", 20);

        assertEquals(transferId.isPresent(), true);

        boolean acceptedTransfer = bank.acceptTransfer(1, "account2", transferId.get());
        assertEquals(acceptedTransfer, true);

        boolean acceptAgain = bank.acceptTransfer(1, "account2", transferId.get());
        assertEquals(acceptAgain, false);
    }

    @Test
    @Order(15)
    void testTransferNoMatchingId() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");
        bank.createAccount(0, "account2");
        bank.deposit(0, "account1", 40);

        Optional<String> transferId = bank.initiateTransfer(0, "account1", "account2", 20);

        assertEquals(transferId.isPresent(), true);

        boolean acceptedTransfer = bank.acceptTransfer(1, "account2", transferId.get() + "1");
        assertEquals(acceptedTransfer, false);
    }

    @Test
    @Order(16)
    void testTransferExpired() {
        final Bank bank = new Bank();
        bank.createAccount(0, "account1");
        bank.createAccount(0, "account2");
        bank.deposit(0, "account1", 40);

        Optional<String> transferId = bank.initiateTransfer(0, "account1", "account2", 20);

        assertEquals(transferId.isPresent(), true);

        boolean acceptedTransfer = bank.acceptTransfer(172800001, "account2", transferId.get());
        assertEquals(acceptedTransfer, false);
    }
}
