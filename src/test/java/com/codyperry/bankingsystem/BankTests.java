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
}
