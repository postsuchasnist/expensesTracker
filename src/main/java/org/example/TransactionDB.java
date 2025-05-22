package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class that connects to the database and extracts all transactions there
 */

public class TransactionDB {
    public static List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Connection connection = DataBaseConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        try{
            statement = connection.prepareStatement("SELECT * FROM transactions");
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String description = resultSet.getString("description");
                double amount = resultSet.getDouble("amount");
                Date date = resultSet.getDate("date");
                Transaction transaction = new Transaction(id, type, description, amount, date);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}
