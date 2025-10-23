package com.codyperry.bankingsystem.model;

public class Transfer {
    private int timestamp;
    private String sourceAccountId;
    private String targetAccountId;
    private int amount;
    private TransferStatus status;

    public Transfer(int timestamp, String sourceAccountId, String targetAccountId, int amount) {
        this.timestamp = timestamp;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;

        this.status = TransferStatus.PENDING;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public int getAmount() {
        return amount;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
}
