package com.codyperry.bankingsystem.model;

public class Transaction {
    private int timestamp;
    private String accountId;
    private int amount;

    public Transaction(int timestamp, String accountId, int amount) {
        this.timestamp = timestamp;
        this.accountId = accountId;
        this.amount = amount;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getAmount() {
        return amount;
    }
}
