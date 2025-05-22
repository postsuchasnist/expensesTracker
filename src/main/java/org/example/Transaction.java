package org.example;

import java.util.Date;

/**
 * class which describes a Transaction and its attributes
 */

public class Transaction {
    private int id;
    private String type;
    private String description;
    private double amount;
    Date date;

    public Transaction(int id, String type, String description, double amount, Date date) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }
    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
    public double getAmount() {
        return amount;
    }
    public Date getDate() {
        return date;
    }
}
