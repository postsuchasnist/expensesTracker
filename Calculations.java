package org.example;

import java.util.List;

/**
 * class responsible for calculating total incomes, expenses and their difference
 */

public class Calculations {
    /**
     * method which calculates a total income
     * @param transactions
     * @return
     */
    public static double getTotalIncomes(List<Transaction> transactions) {
        double total = 0;
        for (Transaction transaction : transactions) {
            if("Income".equals(transaction.getType())){
                total += transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * method to calculate total expenses
     * @param transactions
     * @return
     */
    public static double getTotalExpenses(List<Transaction> transactions) {
        double total = 0;
        for (Transaction transaction : transactions) {
            if("Expense".equals(transaction.getType())){
                total += transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * method to calculate pure income - difference between income and expenses
     * @param transactions
     * @return
     */
    public static double getPureIncome(List<Transaction> transactions) {
        double income = getTotalIncomes(transactions);
        double expense = getTotalExpenses(transactions);
        return income - expense;
    }

    /**
     * method to calculate an average income
     * @param transactions
     * @return
     */
    public static double getAverageIncome(List<Transaction> transactions) {
        if(transactions.isEmpty()){
            return 0;
        }
        return getTotalIncomes(transactions) / transactions.size();
    }

    /**
     * method to calculate an average expenses
     * @param transactions
     * @return
     */
    public static double getAverageExpenses(List<Transaction> transactions) {
        if(transactions.isEmpty()){
            return 0;
        }
        return getTotalExpenses(transactions) / transactions.size();
    }
}
