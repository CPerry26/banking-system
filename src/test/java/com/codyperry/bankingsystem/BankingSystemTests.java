package com.codyperry.bankingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankingSystemTests {
    @Test
    @Order(1)
    void testTopActivityHappyPath() {
        final BankingSystemImpl bankingSystem = new BankingSystemImpl();
        bankingSystem.createAccount(0, "account1");
        bankingSystem.deposit(0, "account1", 100);
        List<String> topActivity = bankingSystem.topActivity(0, 5);

        assertEquals(topActivity.size(), 1);
        assertEquals(topActivity.get(0), "account1(100)");
    }

    @Test
    @Order(2)
    void testTopActivitySorting() {
        final BankingSystemImpl bankingSystem = new BankingSystemImpl();
        bankingSystem.createAccount(0, "account1");
        bankingSystem.deposit(0, "account1", 100);
        bankingSystem.createAccount(0, "account2");
        bankingSystem.deposit(0, "account2", 200);

        List<String> topActivity = bankingSystem.topActivity(0, 5);

        assertEquals(topActivity.size(), 2);
        assertEquals(topActivity.get(0), "account2(200)");
        assertEquals(topActivity.get(1), "account1(100)");
    }

    @Test
    @Order(3)
    void testTopActivitySortingClash() {
        final BankingSystemImpl bankingSystem = new BankingSystemImpl();
        bankingSystem.createAccount(0, "account1");
        bankingSystem.deposit(0, "account1", 100);
        bankingSystem.createAccount(0, "account2");
        bankingSystem.deposit(0, "account2", 100);

        List<String> topActivity = bankingSystem.topActivity(0, 5);

        assertEquals(topActivity.size(), 2);
        assertEquals(topActivity.get(0), "account1(100)");
        assertEquals(topActivity.get(1), "account2(100)");
    }
}
