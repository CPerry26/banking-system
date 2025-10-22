package com.codyperry.bankingsystem;

import java.util.List;
import java.util.Optional;

/**
 * A simple banking system allowing account creation, transactions, and finding the top activity.
 */
public interface BankingSystem {
    /**
     * Create a new account at the bank. This returns false if the account with the specified ID already exists.
     *
     * @param timestamp
     * @param accountId
     *
     * @return True if created, false otherwise
     */
    boolean createAccount(int timestamp, String accountId);

    /**
     * Close the specified account if it exists.
     *
     * @param timestamp
     * @param accountId
     *
     * @return The remaining balance on the account.
     */
    Optional<Integer> closeAccount(int timestamp, String accountId);

    /**
     * Deposit the amount into the specified account ID. Returns an empty optional if the account does not exist.
     *
     * @param timestamp
     * @param accountId
     * @param amount
     *
     * @return The current balance of the account after deposit
     */
    Optional<Integer> deposit(int timestamp, String accountId, int amount);

    /**
     * Send a payment of the amount out of the specified account. This returns an empty optional if the account does not
     * exist or has insufficient funds.
     *
     * @param timestamp
     * @param accountId
     * @param amount
     *
     * @return The current balance after deduction
     */
    Optional<Integer> pay(int timestamp, String accountId, int amount);

    /**
     * Get the top N transaction totals at the bank, including payments (count as additions and not subtractions). This
     * should be sorted by descending total and contain an array of account IDs with totals, i.e. account1(10000).
     *
     * @param timestamp
     * @param n The number of top accounts to retrieve.
     *
     * @return A list of N top transaction accounts
     */
    List<String> topActivity(int timestamp, int n);
}
