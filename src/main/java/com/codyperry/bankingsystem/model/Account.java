package com.codyperry.bankingsystem.model;

public class Account {
    private String accountId;
    private Integer balance;
    private int created;

    public Account(String accountId, int createdTimestamp) {
        this.accountId = accountId;
        this.balance = 0;
        this.created = createdTimestamp;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public Integer updateBalance(Integer amount) {
        this.balance = this.balance + amount;
        return this.balance;
    }

    public Integer getCurrentBalance() {
        return this.balance;
    }

    public int getCreationTime() { return this.created; }
}
