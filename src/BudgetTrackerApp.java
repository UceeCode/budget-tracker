import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BudgetTrackerApp extends JFrame {
    expenses = new ArrayList<>();

    // Set up the UI
    setTitle("Household Budget Tracker");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(600, 400);
    setLayout(new BorderLayout());


    // Create the table for displaying expenses
    String[] columns = {"Date", "Description", "Amount"};
    tableModel = new DefaultTableModel(columns, 0);
    expenseTable = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(expenseTable);
    add(scrollPane, BorderLayout.CENTER);

    // Add buttons for adding and editing expenses
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton addButton = new JButton("Add Expense");
    JButton editButton = new JButton("Edit Expense");
    JButton addIncomeButton = new JButton("Add Income");
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(addIncomeButton);
    add(buttonPanel, BorderLayout.SOUTH);

}