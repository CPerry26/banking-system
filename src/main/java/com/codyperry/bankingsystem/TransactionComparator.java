package com.codyperry.bankingsystem;

import java.util.Comparator;
import java.util.Map;

/**
 * Custom comparator to sort by value first in descending order then name in ascending order.
 */
public class TransactionComparator implements Comparator<Map.Entry<String, Integer>> {
    @Override
    public int compare(Map.Entry<String, Integer> leftEntry, Map.Entry<String, Integer> rightEntry) {
        int valueCompare = rightEntry.getValue().compareTo(leftEntry.getValue());
        int nameCompare = leftEntry.getKey().compareTo(rightEntry.getKey());

        return (valueCompare == 0) ? nameCompare : valueCompare;
    }
}
