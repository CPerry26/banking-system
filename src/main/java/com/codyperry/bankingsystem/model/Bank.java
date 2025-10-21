package com.codyperry.bankingsystem.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Bank {
    // Account ID mapped to the current balance
    private Map<String, Account> accounts;
    private Map<String, Integer> transactions;

    public Bank() {
        // This is not thread safe.
        this.accounts = new HashMap<>();
        this.transactions = new HashMap<>();
    }

    public boolean createAccount(int timestamp, String accountId) {
        if (this.accounts.containsKey(accountId)) {
            return false;
        }

        Account newAccount = new Account(accountId, timestamp);

        this.accounts.put(accountId, newAccount);
        this.transactions.put(accountId, 0);

        return true;
    }

    public Optional<Integer> deposit(int timestamp, String accountId, int amount) {
        if (!this.accounts.containsKey(accountId)) {
            return Optional.empty();
        }

        Integer balance = this.accounts.get(accountId).updateBalance(amount);

        this.transactions.compute(accountId, (k, v) -> v + amount);

        return Optional.of(balance);
    }

    public Optional<Integer> pay(int timestamp, String accountId, int amount) {
        if (!this.accounts.containsKey(accountId) || (this.getBalance(timestamp, accountId).get()) < amount) {
            return Optional.empty();
        }

        Integer balance = this.accounts.get(accountId).updateBalance(-amount);

        this.transactions.compute(accountId, (k, v) -> v + amount);

        return Optional.of(balance);
    }

    public Optional<Integer> getBalance(int timestamp, String accountId) {
        if (!this.accounts.containsKey(accountId)) {
            return Optional.empty();
        }

        return Optional.of(this.accounts.get(accountId).getCurrentBalance());
    }

    public Map<String, Account> getAccounts() {
        return this.accounts;
    }

    public Map<String, Integer> getTransactions() {
        return this.transactions;
    }
}
