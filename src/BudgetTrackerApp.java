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

    // Add labels for displaying total income, total expense, and remaining money
    JPanel totalPanel = new JPanel(new GridLayout(3, 2));
    totalPanel.setBorder(BorderFactory.createTitledBorder("Budget Summary"));
    totalPanel.setPreferredSize(new Dimension(200, 120));
    totalPanel.add(new JLabel("Total Income: "));
    totalIncomeLabel = new JLabel("0.0");
    totalPanel.add(totalIncomeLabel);
    totalPanel.add(new JLabel("Total Expense: "));
    totalExpenseLabel = new JLabel("0.0");
    totalPanel.add(totalExpenseLabel);
    totalPanel.add(new JLabel("Remaining: "));
    totalRemainingLabel = new JLabel("0.0");
    totalPanel.add(totalRemainingLabel);
    add(totalPanel, BorderLayout.NORTH);

    // Load saved expenses from file
    loadExpenses();

    // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAddExpenseDialog();
        }
    });

        editButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = expenseTable.getSelectedRow();
            if (selectedRow >= 0) {
                showEditExpenseDialog(selectedRow);
            } else {
                JOptionPane.showMessageDialog(BudgetTrackerApp.this, "Please select an expense to edit.");
            }
        }
    });

        addIncomeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAddIncomeDialog();
        }
    });
}

// Method to display the "Add Expense" dialog
private void showAddExpenseDialog() {
    AddExpenseDialog dialog = new AddExpenseDialog(this);
    dialog.setVisible(true);
    if (dialog.isConfirmed()) {
        Expense expense = dialog.getExpense();
        expenses.add(expense);
        addExpenseToTable(expense);
        updateBudgetSummary();
        saveExpenses();
    }
}


}