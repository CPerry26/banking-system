package com.codyperry.bankingsystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Bank {
    // Account ID mapped to the current balance
    private Map<String, Account> accounts;

    // Account ID mapped to a list of transactions made on the account.
    private Map<String, List<Transaction>> transactions;

    // Transfer ID mapped to the pending transfer.
    private Map<String, Transfer> transfers;

    private final int MAX_TRANSFER_LIFETIME = 172800000;

    public Bank() {
        // This is not thread safe.
        this.accounts = new HashMap<>();
        this.transactions = new HashMap<>();
        this.transfers = new HashMap<>();
    }

    public boolean createAccount(int timestamp, String accountId) {
        if (this.accounts.containsKey(accountId)) {
            return false;
        }

        Account newAccount = new Account(accountId, timestamp);

        this.accounts.put(accountId, newAccount);
        this.transactions.put(accountId, new ArrayList<>());

        return true;
    }

    public Optional<Integer> deposit(int timestamp, String accountId, int amount) {
        if (!this.accounts.containsKey(accountId)) {
            return Optional.empty();
        }

        Integer balance = this.accounts.get(accountId).deposit(amount);

        Transaction transaction = new Transaction(timestamp, accountId, amount);

        List<Transaction> acctTransactions = this.transactions.get(accountId);
        acctTransactions.add(transaction);

        this.transactions.put(accountId, acctTransactions);

        return Optional.of(balance);
    }

    public Optional<Integer> pay(int timestamp, String accountId, int amount) {
        if (!this.accounts.containsKey(accountId) || (this.getBalance(timestamp, accountId).get()) < amount) {
            return Optional.empty();
        }

        Integer balance = this.accounts.get(accountId).withdraw(amount);

        Transaction transaction = new Transaction(timestamp, accountId, amount);

        List<Transaction> acctTransactions = this.transactions.get(accountId);
        acctTransactions.add(transaction);

        this.transactions.put(accountId, acctTransactions);

        return Optional.of(balance);
    }

    public Optional<Integer> closeAccount(int timestamp, String accountId) {
        if (!this.accounts.containsKey(accountId)) {
            return Optional.empty();
        }

        // Get the account but don't remove from transactions so we have history.
        Account account = this.accounts.get(accountId);
        this.accounts.remove(accountId);

        return Optional.of(account.getCurrentBalance());
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

    public Map<String, List<Transaction>> getTransactions() {
        return this.transactions;
    }

    public Optional<String> initiateTransfer(int timestamp, String sourceAccountId, String targetAccountId, int amount) {
        if (!this.accounts.containsKey(sourceAccountId) || !this.accounts.containsKey(targetAccountId)) {
            return Optional.empty();
        }

        // If the source account has insufficient funds, reject.
        if (this.getBalance(timestamp, sourceAccountId).get() < amount) {
            return Optional.empty();
        }

        Transfer transfer = new Transfer(timestamp, sourceAccountId, targetAccountId, amount);
        String transferId = "transfer" + this.transfers.size() + 1 + "";

        this.transfers.put(transferId, transfer);

        return Optional.of(transferId);
    }

    public boolean acceptTransfer(int timestamp, String accountId, String transferId) {
        if (!this.accounts.containsKey(accountId) || !this.transfers.containsKey(transferId)) {
            return false;
        }

        Transfer transfer = this.transfers.get(transferId);

        // Make sure the transfer hasn't already been accepted or expired.
        if (transfer.getStatus() == TransferStatus.ACCEPTED || transfer.getStatus() == TransferStatus.EXPIRED) {
            return false;
        }

        // Make sure it hasn't expired.
        if (timestamp > (transfer.getTimestamp() + MAX_TRANSFER_LIFETIME)) {
            transfer.setStatus(TransferStatus.EXPIRED);

            this.transfers.put(transferId, transfer);

            return false;
        }

        // Not expired, set the status to accepted.
        transfer.setStatus(TransferStatus.ACCEPTED);

        // Update the source and target account balances.
        this.pay(timestamp, transfer.getSourceAccountId(), transfer.getAmount());
        this.deposit(timestamp, transfer.getTargetAccountId(), transfer.getAmount());

        // Put the transfer back.
        this.transfers.put(transferId, transfer);

        return true;
    }
}
