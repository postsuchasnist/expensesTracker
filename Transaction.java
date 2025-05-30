package org.example;

import java.time.LocalDate;
import java.util.Date;

/**
 * class which describes a Transaction and its attributes
 */

public class Transaction {
    private int id;
    private String type;
    private String description;
    private double amount;
    private String date;

    /**
     * class constructor
     * @param id
     * @param type
     * @param description
     * @param amount
     * @param date
     */
    public Transaction(int id, String type, String description, double amount, String date) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    /**
     * method to obtain an id of the transaction
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * method to get type of the transaction
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * method to get description of the transaction
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * method to get amount of the transaction
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * method to get date of the transaction
     * @return date
     */
    public String getDate() {
        return date;
    }
}
