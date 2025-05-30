package org.example;

/**
 * a class which converts an amount between the currencies czk, euro and dollar.
 * All rates are taking from google
 */

public class CurrencyConverter {
    /**
     * method to convert from dollar to euro
     * @param amount
     * @return
     */
    public static double DollarToEuro(double amount) {
        return amount * 0.88;
    }

    /**
     * method to convert from euro to dollar
     * @param amount
     * @return
     */
    public static double EuroToDollar(double amount) {
        return amount / 0.88;
    }

    /**
     * method to convert from dollar to czk
     * @param amount
     * @return
     */
    public static double DollarToCzk(double amount) {
        return amount * 22;
    }

    /**
     * method to convert from czk to dollar
     * @param amount
     * @return
     */
    public static double CzkToDollar(double amount) {
        return amount / 22;
    }

    /**
     * method to convert from euro to czk
     * @param amount
     * @return
     */
    public static double EuroToCzk(double amount) {
        return amount * 25;
    }

    /**
     * method to convert from czk to euro
     * @param amount
     * @return
     */
    public static double CzkToEuro(double amount) {
        return amount / 25;
    }
}
