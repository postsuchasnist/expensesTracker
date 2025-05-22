package org.example;

import java.util.List;

/**
 * class responsible for calculating total incomes, expenses and their difference
 */

public class Calculations {
    public static double getTotalIncomes(List<Transaction> transactions) {
        double total = 0;
        for (Transaction transaction : transactions) {
            if("Income".equals(transaction.getType())){
                total += transaction.getAmount();
            }
        }
        return total;
    }

    public static double getTotalExpenses(List<Transaction> transactions) {
        double total = 0;
        for (Transaction transaction : transactions) {
            if("Expense".equals(transaction.getType())){
                total += transaction.getAmount();
            }
        }
        return total;
    }

    public static double getPureIncome(List<Transaction> transactions) {
        double income = getTotalIncomes(transactions);
        double expense = getTotalExpenses(transactions);
        return income - expense;
    }
}
