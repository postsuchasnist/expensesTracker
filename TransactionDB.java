package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class that stores a list of transactions
 */

public class TransactionDB {
    private static List<Transaction> transactions;

    /**
     * class constructor, simply defines a new array list
     */
    public TransactionDB() {
        transactions = new ArrayList<>();
    }

    /**
     * method to add transaction to the list
     * @param transaction
     */
    public static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * method to remove transaction from the list
     * @param transaction
     */
    public static void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    /**
     * method that returns all transactions in the list
     * @return
     */
    public static List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * method to get transactions which describe expenses
     * @return
     */
    public static List<Transaction> getTransactionsExpenses() {
        List<Transaction> expenses = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if(transaction.getType().equals("Expense")) {
                expenses.add(transaction);
            }
        }
        return expenses;
    }

    /**
     * method to get transactions which describe incomes
     * @return
     */
    public static List<Transaction> getTransactionsIncome() {
        List<Transaction> income = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if(transaction.getType().equals("Income")) {
                income.add(transaction);
            }
        }
        return income;
    }
}
