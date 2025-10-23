package com.codyperry.bankingsystem;

import com.codyperry.bankingsystem.model.Bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BankingSystemImpl implements BankingSystem {
    private Bank bank;

    public BankingSystemImpl() {
        this.bank = new Bank();
    }

    @Override
    public boolean createAccount(int timestamp, String accountId) {
        return this.bank.createAccount(timestamp, accountId);
    }

    @Override
    public Optional<Integer> closeAccount(int timestamp, String accountId) {
        return this.bank.closeAccount(timestamp, accountId);
    }

    @Override
    public Optional<Integer> deposit(int timestamp, String accountId, int amount) {
        return this.bank.deposit(timestamp, accountId, amount);
    }

    @Override
    public Optional<Integer> pay(int timestamp, String accountId, int amount) {
        return this.bank.pay(timestamp, accountId, amount);
    }

    @Override
    public List<String> topActivity(int timestamp, int n) {
        List<Map.Entry<String, Integer>> sortedActivity = new ArrayList<>(this.bank.getTransactions().entrySet());
        Collections.sort(sortedActivity, new TransactionComparator());

        List<String> resultList = new ArrayList<>();
        for (int idx = 0; idx < n && idx < sortedActivity.size(); idx++) {
            Map.Entry<String, Integer> entry = sortedActivity.get(idx);

            resultList.add(entry.getKey() + "(" + entry.getValue() + ")");
        }

        return resultList;
    }

    @Override
    public Optional<String> transfer(int timestamp, String sourceAccountId, String targetAccountId, int amount) {
        return this.bank.initiateTransfer(timestamp, sourceAccountId, targetAccountId, amount);
    }

    @Override
    public boolean acceptTransfer(int timestamp, String accountId, String transferId) {
        return this.bank.acceptTransfer(timestamp, accountId, transferId);
    }
}
