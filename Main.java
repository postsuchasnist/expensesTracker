package org.example;


import org.jdatepicker.JDatePanel;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Main class that is responsible for creating the GUI and running the application
 * it uses classes such as Renderer, CustomScrollBar, DateLabelFormatter,
 * Transaction, TransactionDB, Calculations
 */

public class Main {
    private final JFrame frame;
    private final JPanel dashboardPanel;
    private JPanel buttonsPanel;
    private JButton addTransactionButton;
    private JButton removeTransactionButton;
    private final JTable transactionTable;
    private final DefaultTableModel tableModel;
    private double totalAmount = 0.0;
    private final ArrayList<String> dataPanelValues = new ArrayList<>();
    private int id = 0;
    private TransactionDB transactionDB = new TransactionDB();;
    private double averageIncome = 0.0;
    private double averageExpenses = 0.0;
    private double totalIncome = 0.0;
    private double totalExpenses = 0.0;
    private JButton lightMode;
    private boolean light = true;
    private JComboBox comboBox;
    private boolean dollar = true;
    private boolean euro = false;
    private boolean czk = false;
    /**
     * main function that creates the window, adds panels, buttons, light mode switcher,
     * clock, table of transactions
     */
    public Main(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);


        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        dashboardPanel.setBackground(new Color(210, 210, 255));
        frame.add(dashboardPanel,BorderLayout.CENTER);

        totalAmount = Calculations.getPureIncome(TransactionDB.getTransactions());
        dataPanelValues.add(String.format("-$%,.2f", Calculations.getTotalExpenses(TransactionDB.getTransactions())));
        dataPanelValues.add(String.format("$%,.2f", Calculations.getTotalIncomes(TransactionDB.getTransactions())));
        dataPanelValues.add("$"+totalAmount);

        averageIncome = Calculations.getAverageIncome(TransactionDB.getTransactions());
        dataPanelValues.add(String.format("$%,.2f", averageIncome));

        averageExpenses = Calculations.getAverageExpenses(TransactionDB.getTransactions());
        dataPanelValues.add(String.format("$%,.2f", averageExpenses));


        addDataPanel("Expense", 0);
        addDataPanel("Income", 1);
        addDataPanel("Total", 2);
        addDataPanel("Average Income", 3);
        addDataPanel("Average Expenses", 4);

        addTransactionButton = new JButton("Add Transaction");
        addTransactionButton.setBackground(new Color(230, 230, 230));
        addTransactionButton.setForeground(Color.BLUE);
        addTransactionButton.setFocusPainted(false);
        addTransactionButton.setBorderPainted(false);
        addTransactionButton.setFont(new Font("Arial", Font.BOLD, 14));
        addTransactionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addTransactionButton.addActionListener((e) -> { showAddTransactionDialog(); });

        removeTransactionButton = new JButton("Remove Transaction");
        removeTransactionButton.setBackground(new Color(230, 230, 230));
        removeTransactionButton.setForeground(Color.RED);
        removeTransactionButton.setFocusPainted(false);
        removeTransactionButton.setBorderPainted(false);
        removeTransactionButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeTransactionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeTransactionButton.addActionListener((e) -> {
            removeSelectedTransaction();
        });

        lightMode = new JButton("Light");
        lightMode.setBackground(new Color(255, 255, 255));
        lightMode.setForeground(Color.BLACK);
        lightMode.setFocusPainted(false);
        lightMode.setBorderPainted(false);
        lightMode.setFont(new Font("Arial", Font.BOLD, 14));
        lightMode.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lightMode.addActionListener((e) -> {
            changeMode();
        });

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout(10, 5));
        buttonsPanel.add(addTransactionButton, BorderLayout.NORTH);
        buttonsPanel.add(removeTransactionButton, BorderLayout.SOUTH);
        buttonsPanel.setBackground(new Color(255, 255, 255));
        dashboardPanel.add(buttonsPanel);

        comboBox = new JComboBox(new String[]{"USD", "Euro", "Czk"});
        dashboardPanel.add(comboBox);
        comboBox.addActionListener((e) -> update());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout(20,20));
        buttonPanel.setBackground(new Color(210, 210, 255));
        buttonPanel.add(lightMode);
        dashboardPanel.add(buttonPanel);

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setForeground(Color.BLACK);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        Timer timer = new Timer(1000, (e) -> {
            Date date = new Date();
            timeLabel.setText(sdf.format(date));
        });
        timer.start();

        dashboardPanel.add(timeLabel);

        // Set up the transaction table
        String[] columnNames = {"ID","Type","Description","Amount","Date"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                // Make all cells non-editable
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        configureTransactionTable();

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        configureScrollPane(scrollPane);
        dashboardPanel.add(scrollPane);


        frame.setVisible(true);
    }

    /**
     * method that updates values of the amounts according to the specified currency
     * initially, the currency is dollars, by choosing euro or czk it will show the corresponding amount
     * in the total, income, expenses boxes
     */

    private void update(){
        String value = comboBox.getSelectedItem().toString();
        char symbol = dataPanelValues.get(0).charAt(0);
        if(value.equals("USD")){
            dollar = true;
            euro = false;
            czk = false;
            double newTotal = totalAmount;
            double newIncome = totalIncome;
            double newExpenses = totalExpenses;
            double newAverageIncome = averageIncome;
            double newAverageExpenses = averageExpenses;
            if(symbol=='€'){
                newTotal = CurrencyConverter.EuroToDollar(totalAmount);
                newIncome = CurrencyConverter.EuroToDollar(totalIncome);
                newExpenses = CurrencyConverter.EuroToDollar(totalExpenses);
                newAverageExpenses = CurrencyConverter.EuroToDollar(averageExpenses);
                newAverageIncome = CurrencyConverter.EuroToDollar(averageIncome);
            } else if(symbol=='C'){
                newTotal = CurrencyConverter.CzkToDollar(totalAmount);
                newIncome = CurrencyConverter.CzkToDollar(totalIncome);
                newExpenses = CurrencyConverter.CzkToDollar(totalExpenses);
                newAverageExpenses = CurrencyConverter.CzkToDollar(averageExpenses);
                newAverageIncome = CurrencyConverter.CzkToDollar(averageIncome);
            }
            totalAmount = newTotal;
            totalIncome = newIncome;
            totalExpenses = newExpenses;
            averageIncome = newAverageIncome;
            averageExpenses = newAverageExpenses;
            JPanel expense = (JPanel) dashboardPanel.getComponent(0);
            expense.repaint();
            JPanel income = (JPanel) dashboardPanel.getComponent(1);
            income.repaint();
            JPanel total = (JPanel) dashboardPanel.getComponent(2);
            total.repaint();
            JPanel averageExpense = (JPanel) dashboardPanel.getComponent(3);
            averageExpense.repaint();
            JPanel averageIncome = (JPanel) dashboardPanel.getComponent(4);
            averageIncome.repaint();

            dataPanelValues.set(0, "-" + fixNegativeValueDisplay(newExpenses));
            dataPanelValues.set(1, String.format("$%,.2f", newIncome));
            dataPanelValues.set(2, String.format("$%,.2f", newTotal));
            dataPanelValues.set(3, String.format("$%,.2f", newAverageIncome));
            dataPanelValues.set(4, "-" + fixNegativeValueDisplay(newAverageExpenses));
        } else if(value.equals("Euro")){
            dollar = false;
            euro = true;
            czk = false;

            double newTotal = totalAmount;
            double newIncome = totalIncome;
            double newExpenses = totalExpenses;
            double newAverageIncome = averageIncome;
            double newAverageExpenses = averageExpenses;
            if(symbol=='$' || symbol=='-'){
                newTotal = CurrencyConverter.DollarToEuro(totalAmount);
                newIncome = CurrencyConverter.DollarToEuro(totalIncome);
                newExpenses = CurrencyConverter.DollarToEuro(totalExpenses);
                newAverageExpenses = CurrencyConverter.DollarToEuro(averageExpenses);
                newAverageIncome = CurrencyConverter.DollarToEuro(averageIncome);
            } else if(symbol=='C'){
                newTotal = CurrencyConverter.CzkToEuro(totalAmount);
                newIncome = CurrencyConverter.CzkToEuro(totalIncome);
                newExpenses = CurrencyConverter.CzkToEuro(totalExpenses);
                newAverageExpenses = CurrencyConverter.CzkToEuro(averageExpenses);
                newAverageIncome = CurrencyConverter.CzkToEuro(averageIncome);
            }
            totalAmount = newTotal;
            totalIncome = newIncome;
            totalExpenses = newExpenses;
            averageIncome = newAverageIncome;
            averageExpenses = newAverageExpenses;
            JPanel expense = (JPanel) dashboardPanel.getComponent(0);
            expense.repaint();
            JPanel income = (JPanel) dashboardPanel.getComponent(1);
            income.repaint();
            JPanel total = (JPanel) dashboardPanel.getComponent(2);
            total.repaint();
            JPanel averageExpense = (JPanel) dashboardPanel.getComponent(3);
            averageExpense.repaint();
            JPanel averageIncome = (JPanel) dashboardPanel.getComponent(4);
            averageIncome.repaint();

            dataPanelValues.set(0, "-" + fixNegativeValueDisplay(newExpenses));
            dataPanelValues.set(1, String.format("€%,.2f", newIncome));
            dataPanelValues.set(2, String.format("€%,.2f", newTotal));
            dataPanelValues.set(3, String.format("€%,.2f", newAverageIncome));
            dataPanelValues.set(4, "-" + fixNegativeValueDisplay(newAverageExpenses));


        } else if(value.equals("Czk")){
            dollar = false;
            euro = false;
            czk = true;

            double newTotal = totalAmount;
            double newIncome = totalIncome;
            double newExpenses = totalExpenses;
            double newAverageIncome = averageIncome;
            double newAverageExpenses = averageExpenses;
            if(symbol=='$' || symbol=='-'){
                newTotal = CurrencyConverter.DollarToCzk(totalAmount);
                newIncome = CurrencyConverter.DollarToCzk(totalIncome);
                newExpenses = CurrencyConverter.DollarToCzk(totalExpenses);
                newAverageExpenses = CurrencyConverter.DollarToCzk(averageExpenses);
                newAverageIncome = CurrencyConverter.DollarToCzk(averageIncome);
            } else if(symbol=='€'){
                newTotal = CurrencyConverter.EuroToCzk(totalAmount);
                newIncome = CurrencyConverter.EuroToCzk(totalIncome);
                newExpenses = CurrencyConverter.EuroToCzk(totalExpenses);
                newAverageExpenses = CurrencyConverter.EuroToCzk(averageExpenses);
                newAverageIncome = CurrencyConverter.EuroToCzk(averageIncome);
            }
            totalAmount = newTotal;
            totalIncome = newIncome;
            totalExpenses = newExpenses;
            averageExpenses = newAverageExpenses;
            averageIncome = newAverageIncome;
            JPanel expense = (JPanel) dashboardPanel.getComponent(0);
            expense.repaint();
            JPanel income = (JPanel) dashboardPanel.getComponent(1);
            income.repaint();
            JPanel total = (JPanel) dashboardPanel.getComponent(2);
            total.repaint();
            JPanel averageExpense = (JPanel) dashboardPanel.getComponent(3);
            averageExpense.repaint();
            JPanel averageIncome = (JPanel) dashboardPanel.getComponent(4);
            averageIncome.repaint();

            dataPanelValues.set(0, "-" + fixNegativeValueDisplay(newExpenses));
            dataPanelValues.set(1, String.format("Czk %,.2f", newIncome));
            dataPanelValues.set(2, String.format("Czk %,.2f", newTotal));
            dataPanelValues.set(3, String.format("Czk %,.2f", newAverageIncome));
            dataPanelValues.set(4, "-" + fixNegativeValueDisplay(newAverageExpenses));
        }

        //System.out.println(dataPanelValues.get(1));
    }

    /**
     * changeMode function is basically the light mode switcher
     * when pressed the display changes to the respective dark/light mode
     */
    private void changeMode(){
        if(light){
            light = false;
            lightMode.setText("Dark");
            lightMode.setBackground(Color.BLACK);
            lightMode.setForeground(Color.WHITE);

            removeTransactionButton.setBackground(new Color(128, 130, 150));
            removeTransactionButton.setForeground(new Color(255, 60, 60));
            addTransactionButton.setBackground(new Color(128, 130, 150));
            addTransactionButton.setForeground(new Color(60, 60, 255));
            buttonsPanel.setBackground(Color.GRAY);

            transactionTable.setBackground(new Color(128, 128, 200));

            dashboardPanel.setBackground(Color.GRAY);
        } else{
            light = true;
            lightMode.setText("Light");
            lightMode.setBackground(new Color(255, 255, 255));
            lightMode.setForeground(Color.BLACK);

            removeTransactionButton.setBackground(new Color(220, 220, 220));
            removeTransactionButton.setForeground(Color.RED);
            addTransactionButton.setBackground(new Color(220, 220, 220));
            addTransactionButton.setForeground(Color.BLUE);
            buttonsPanel.setBackground(Color.WHITE);

            transactionTable.setBackground(new Color(210,210,100));

            dashboardPanel.setBackground(new Color(210, 210, 255));
        }
    }

    /**
     * method that adds each available transaction to the table
     */
    public void populateTableTransactions(){
        for(Transaction transaction : TransactionDB.getTransactions()){
            Object[] rowData = { transaction.getId(), transaction.getType(),
                    transaction.getDescription(), transaction.getAmount(), transaction.getDate()};
            tableModel.addRow(rowData);
        }
    }

    /**
     * method that configures a transaction table
     * it sets color, size and so on
     */

    public void configureTransactionTable(){
        transactionTable.setBackground(new Color(210,210,100));
        transactionTable.setRowHeight(30);
        transactionTable.setShowGrid(false);
        transactionTable.setBorder(null);
        transactionTable.setFont(new Font("Arial",Font.ITALIC,16));
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        populateTableTransactions();

        JTableHeader tableHeader = transactionTable.getTableHeader();
        tableHeader.setForeground(Color.red);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 18));
        tableHeader.setDefaultRenderer(new Renderer());
    }

    /**
     * method which converts a string starting with $- into a double. Needed when we update sums of
     * incomes and updates
     * @param value
     * @return the number in double format
     */
    private String fixNegativeValueDisplay(double value){
        String newVal = "";
        if(dollar){
            newVal = String.format("$%.2f", value);
        } else if(euro){
            newVal = String.format("€%.2f", value);
        } else if(czk){
            newVal = String.format("Czk %.2f", value);
        }
        if(newVal.startsWith("$-")){
            String numericPart = newVal.substring(2);
            newVal = "-$"+numericPart;
        } else if(newVal.startsWith("€-")){
            String numericPart = newVal.substring(2);
            newVal = "-€"+numericPart;
        } else if(newVal.startsWith("Czk -")){
            String numericPart = newVal.substring(5);
            newVal = "-Czk "+numericPart;
        }

        return newVal;
    }

    /**
     * method which removes selected transaction. It retrieves an amount which is needed to be removed
     * and deletes the transaction and decreases its amount from total Value and respective Income or Expense
     */

    private void removeSelectedTransaction(){
        int selectedRow = transactionTable.getSelectedRow();
        if(selectedRow != -1){
            int transactionId = (int) transactionTable.getValueAt(selectedRow, 0);
            String type = transactionTable.getValueAt(selectedRow, 1).toString();
            String amountStr = transactionTable.getValueAt(selectedRow, 3).toString();
            String temp = amountStr.replace("$", "").replace("\u00A0", "").replace(",", "");
            double amount = Double.parseDouble(temp.substring(0, temp.length() - 2));
            if(type.equals("Income")){
                totalAmount -= amount;
                totalIncome -= amount;
                if(transactionDB.getTransactionsIncome().size() - 1 != 0){
                    averageIncome = totalIncome / (TransactionDB.getTransactionsIncome().size() - 1);
                } else{
                    averageIncome = 0;
                }
            }
            else{
                totalAmount += amount;
                totalExpenses -= amount;
                if(transactionDB.getTransactionsExpenses().size() - 1 != 0){
                    averageExpenses = totalExpenses / (TransactionDB.getTransactionsExpenses().size() - 1);
                } else{
                    averageExpenses = 0;
                }
            }
            JPanel totalPanel = (JPanel) dashboardPanel.getComponent(2);
            totalPanel.repaint();
            JPanel averagePanel = (JPanel) dashboardPanel.getComponent(3);
            averagePanel.repaint();
            JPanel expensePanel = (JPanel) dashboardPanel.getComponent(4);
            expensePanel.repaint();
            int indexToUpdate = type.equals("Income") ? 1 : 0;
            String currentValue = dataPanelValues.get(indexToUpdate);
            String tempCurr = currentValue.replace("$", "").replace("\u00A0", "").replace(",", "");
            double currentAmount = Double.parseDouble(tempCurr.substring(0, tempCurr.length() - 2));
            double updatedAmount = currentAmount + (type.equals("Income") ? -amount : amount);
            if(indexToUpdate == 1){
                dataPanelValues.set(indexToUpdate, String.format("$%,.2f", updatedAmount));
                dataPanelValues.set(3, String.format("$%,.2f", averageIncome));
            }
            else{
                dataPanelValues.set(indexToUpdate, fixNegativeValueDisplay(updatedAmount));
                dataPanelValues.set(4, fixNegativeValueDisplay(averageExpenses));
            }

            // repaint the data panel
            JPanel dataPanel = (JPanel) dashboardPanel.getComponent(indexToUpdate);
            dataPanel.repaint();
            tableModel.removeRow(selectedRow);
            removeTransactionFromDatabase(transactionId);
        }
    }
    /**
     * remove a transaction from database
     * @param transactionId
     */
    private void removeTransactionFromDatabase(int transactionId){
        Transaction toRemove = null;
        for(Transaction transaction : transactionDB.getTransactions()){
            if(transaction.getId() == transactionId){
                toRemove = transaction;
            }
        }
        transactionDB.removeTransaction(toRemove);
    }
    /**
     * method which creates a window for adding a new transaction
     */

    private void showAddTransactionDialog(){
        JDialog dialog = new JDialog(frame, "Add Transaction", true);
        dialog.setSize(400,250);
        dialog.setLocationRelativeTo(frame);
        JPanel dialogPanel = new JPanel(new GridLayout(5, 0, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialogPanel.setBackground(new Color(155, 155, 200));
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeCombobox = new JComboBox<>(new String[]{"Expense","Income"});
        typeCombobox.setBackground(Color.WHITE);
        typeCombobox.setBorder(BorderFactory.createLineBorder(Color.yellow));
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();
        descriptionField.setBorder(BorderFactory.createLineBorder(Color.yellow));

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        amountField.setBorder(BorderFactory.createLineBorder(Color.yellow));

        JLabel dateLabel = new JLabel("Date:");
        UtilDateModel utilDateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(utilDateModel, p);
        datePanel.setLocale(Locale.ENGLISH);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setLocale(Locale.ENGLISH);
        datePicker.setBorder(BorderFactory.createLineBorder(Color.yellow));


        // Create and configure the "Add" button
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 0, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener((e) -> {
            addTransaction(typeCombobox, descriptionField, amountField, datePicker);
        });
        dialogPanel.add(typeLabel);
        dialogPanel.add(typeCombobox);
        dialogPanel.add(descriptionLabel);
        dialogPanel.add(descriptionField);
        dialogPanel.add(amountLabel);
        dialogPanel.add(amountField);
        dialogPanel.add(dateLabel);
        dialogPanel.add(datePicker);
        dialogPanel.add(new JLabel());
        dialogPanel.add(addButton);

        //DataBaseConnection.getConnection();

        dialog.add(dialogPanel);
        dialog.setVisible(true);

    }

    /**
     * method which adds a transaction to the database
     * @param typeCombobox
     * @param descriptionField
     * @param amountField
     */
    private void addTransaction(JComboBox<String> typeCombobox, JTextField descriptionField, JTextField amountField, JDatePickerImpl datePicker){
        String type = (String) typeCombobox.getSelectedItem();
        String description = descriptionField.getText();
        String amount = amountField.getText();
        String year = String.valueOf(datePicker.getModel().getYear());
        String month = String.valueOf(datePicker.getModel().getMonth() + 1);
        String day = String.valueOf(datePicker.getModel().getDay());
        String date = year + "-" + month + "-" + day;
        // convert a string into double. the following pattern is used several times
        double newAmount = Double.parseDouble(amount.replace("$", "").replace("\u00A0", "").replace(",", ""));
        if(type.equals("Income")){
            totalAmount += newAmount;
            totalIncome += newAmount;
            averageIncome = totalIncome / (transactionDB.getTransactionsIncome().size() + 1);
        }
        else{
            totalAmount -= newAmount;
            totalExpenses += newAmount;
            averageExpenses = totalExpenses / (transactionDB.getTransactionsExpenses().size() + 1);
        }
        JPanel totalPanel = (JPanel) dashboardPanel.getComponent(2);
        totalPanel.repaint();
        JPanel averagePanel = (JPanel) dashboardPanel.getComponent(3);
        averagePanel.repaint();
        JPanel averageExpense = (JPanel) dashboardPanel.getComponent(4);
        averageExpense.repaint();
        int indexToUpdate = type.equals("Income") ? 1 : 0;
        String currentValue = dataPanelValues.get(indexToUpdate);
        String temp = currentValue.replace("$", "").replace("\u00A0", "").replace(",", "");
        double currentAmount = Double.parseDouble(temp.substring(0, temp.length() - 2));
        double updatedAmount = currentAmount + (type.equals("Income") ? newAmount : -newAmount);
        // Update the data panel with the new amount
        if(indexToUpdate == 1){ // income
            dataPanelValues.set(indexToUpdate, String.format("$%,.2f", updatedAmount));
            dataPanelValues.set(3, String.format("$%,.2f", averageIncome));
        }
        else{
            dataPanelValues.set(indexToUpdate, fixNegativeValueDisplay(updatedAmount));
            dataPanelValues.set(4, fixNegativeValueDisplay(-averageExpenses));
        }
        JPanel dataPanel = (JPanel) dashboardPanel.getComponent(indexToUpdate);
        dataPanel.repaint();

        Transaction transaction = new Transaction(++id, type, description, Double.parseDouble(amount), date);
        transactionDB.addTransaction(transaction);

        tableModel.setRowCount(0);
        populateTableTransactions();

    }

    /**
     * mrthod to add a data panel
     * @param title title of a panel
     * @param index index of a panel
     */
    private void addDataPanel(String title, int index){
        JPanel dataPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if(title.equals("Total")) {
                    drawDataPanel(g2d, title, fixNegativeValueDisplay(totalAmount), getWidth(), getHeight());
                }else if(title.equals("Average")){
                    drawDataPanel(g2d, title, dataPanelValues.get(index), getWidth(), getHeight());
                } else{
                    drawDataPanel(g2d, title, dataPanelValues.get(index), getWidth(), getHeight());
                }
            }
        };
        dataPanel.setLayout(new GridLayout(2, 1));
        dataPanel.setPreferredSize(new Dimension(220, 100));
        dataPanel.setBackground(new Color(100,150,200));
        dataPanel.setForeground(Color.RED);
        dataPanel.setBorder(new LineBorder(new Color(100,150,200),3));
        dashboardPanel.add(dataPanel);
    }
    /**
     * method to draw a data panel
     * @param g graphics
     * @param title title of a panel
     * @param value value of a cell
     * @param width width of a panel
     * @param height height of a panel
     */
    private void drawDataPanel(Graphics g, String title, String value, int width, int height){
        Graphics2D g2d = (Graphics2D)g;
        if(light == true){
            g2d.setColor(new Color(255, 255, 255));
        }else{
            g2d.setColor(new Color(118, 118, 118));
        }
        g2d.fillRoundRect(0, 0, width, height, 20, 20);
        if(light == true){
            g2d.setColor(new Color(236,240,241));
        }else{
            g2d.setColor(new Color(100,100,230));
        }
        g2d.fillRect(0, 0, width, 40);
        if(light == true){
            g2d.setColor(Color.BLACK);
        } else{
            g2d.setColor(Color.WHITE);
        }
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(title, 20, 30);
        if(light == true){
            g2d.setColor(Color.BLACK);
        } else{
            g2d.setColor(Color.WHITE);
        }
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.drawString(value, 20, 75);
    }

    /**
     * method that configures a scroll pane, own implementation using CustomScrollBar class
     * @param scrollPane
     */

    private void configureScrollPane(JScrollPane scrollPane){

        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(750, 300));

    }

    public static void main(String[] args) {
        new Main();
    }
}