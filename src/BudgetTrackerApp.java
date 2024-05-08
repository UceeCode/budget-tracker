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

// Method to display the "Edit Expense" dialog
private void showEditExpenseDialog(int rowIndex) {
    Expense expense = expenses.get(rowIndex);
    EditExpenseDialog dialog = new EditExpenseDialog(this, expense);
    dialog.setVisible(true);
    if (dialog.isConfirmed()) {
        Expense updatedExpense = dialog.getExpense();
        expenses.set(rowIndex, updatedExpense);
        updateExpenseInTable(rowIndex, updatedExpense);
        updateBudgetSummary();
        saveExpenses();
    }
}

// Method to display the "Add Income" dialog
private void showAddIncomeDialog() {
    AddIncomeDialog dialog = new AddIncomeDialog(this);
    dialog.setVisible(true);
    if (dialog.isConfirmed()) {
        Expense income = dialog.getExpense();
        expenses.add(income);
        addIncomeToTable(income);
        updateBudgetSummary();
        saveExpenses();
    }
}

// Method to add an expense to the table
private void addExpenseToTable(Expense expense) {
    if (expense != null) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Object[] rowData = {dateFormat.format(expense.getDate()), expense.getDescription(), expense.getAmount()};
        tableModel.addRow(rowData);
    }
}

// Method to add income to the table
private void addIncomeToTable(Expense income) {
    if (income != null) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Object[] rowData = {dateFormat.format(income.getDate()), income.getDescription(), income.getAmount()};
        tableModel.addRow(rowData);
    }
}

// Method to update an expense in the table
private void updateExpenseInTable(int rowIndex, Expense expense) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Object[] rowData = {dateFormat.format(expense.getDate()), expense.getDescription(), expense.getAmount()};
    for (int i = 0; i < rowData.length; i++) {
        tableModel.setValueAt(rowData[i], rowIndex, i);
    }
}

// Method to load expenses from file
private void loadExpenses() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("expenses.dat"))) {
        List<Expense> loadedExpenses = (List<Expense>) ois.readObject();
        for (Expense expense : loadedExpenses) {
            expenses.add(expense);
            if (expense.getAmount() >= 0) {
                addIncomeToTable(expense);
            } else {
                addExpenseToTable(expense);
            }
        }
        updateBudgetSummary();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}

// Method to save expenses to file
private void saveExpenses() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("expenses.dat"))) {
        oos.writeObject(expenses);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

// Method to update the budget summary labels
private void updateBudgetSummary() {
    double totalIncome = 0;
    double totalExpense = 0;
    for (Expense expense : expenses) {
        if (expense.getAmount() >= 0) {
            totalIncome += expense.getAmount();
        } else {
            totalExpense += expense.getAmount();
        }
    }
    totalIncomeLabel.setText(String.valueOf(totalIncome));
    totalExpenseLabel.setText(String.valueOf(totalExpense));

    // Calculate remaining money
    double remaining = totalIncome + totalExpense;
    totalRemainingLabel.setText(String.valueOf(remaining));
}

// Main method
public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new BudgetTrackerApp().setVisible(true);
        }
    });
}
}

// Expense class representing an expense or income
class Expense implements Serializable {
    private Date date;
    private String description;
    private double amount;

    // Constructor
    public Expense(Date date, String description, double amount) {
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    // Getters and setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}


// Dialog for adding an expense
class AddExpenseDialog extends JDialog {
    protected JTextField dateField;
    protected JTextField descriptionField;
    protected JTextField amountField;
    public boolean confirmed = false;

    // Constructor
    public AddExpenseDialog(Frame parent) {
        super(parent, "Add Expense", true);
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));

        // Add components to the dialog
        add(new JLabel("Date (yyyy-MM-dd):"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField();
        add(descriptionField);

        add(new JLabel("Amount:"));
        amountField = new JTextField();
        add(amountField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                dispose();
            }
        });
        add(addButton);
    }

    public boolean isConfirmed() {
        return confirmed;
    }


    public Expense getExpense() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateField.getText());
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            return new Expense(date, description, -amount); // Negative amount for expense
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

}