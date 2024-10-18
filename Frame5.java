import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Calendar;

public class Frame5 implements ActionListener {
    JFrame frame;
    JLabel heading;
    JButton back;
    JTable table;
    DefaultTableModel model;
    JScrollPane scrollPane;
    JButton closeBtn;
    JComboBox<String> monthComboBox; // Dropdown for selecting months
    JComboBox<Integer> yearComboBox;  // Dropdown for selecting years
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int selectedMonth;
    int selectedYear;
    String username;

    Frame5(String username) {
        this.username = username; // Store the username
        frame = new JFrame("Monthly Expense");
        frame.setSize(600, 600);
        frame.setLocation(250, 150);
        frame.setLayout(null);

        heading = new JLabel("MONTHLY EXPENSE");
        heading.setBounds(20, 17, 320, 40);
        frame.add(heading);

        Font f = new Font("Arial", Font.BOLD, 18);
        heading.setFont(f);
        heading.setForeground(Color.BLUE);

        // Initialize the month dropdown
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setBounds(20, 60, 150, 30);
        monthComboBox.addActionListener(this);
        frame.add(monthComboBox);

        // Initialize the year dropdown
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer[] years = {currentYear, currentYear - 1, currentYear - 2, currentYear - 3, currentYear - 4};
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setBounds(200, 60, 100, 30);
        yearComboBox.addActionListener(this);
        frame.add(yearComboBox);

        model = new DefaultTableModel();
        String[] colName = {"Date", "Description", "Transaction_Type", "Amount"};
        model.setColumnIdentifiers(colName);

        table = new JTable();
        table.setModel(model);

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 100, 550, 400);
        frame.add(scrollPane);

        back = new JButton("BACK");
        back.setBounds(20, 510, 70, 40);
        back.setBackground(Color.BLUE);
        back.setForeground(Color.WHITE);
        frame.add(back);
        back.addActionListener(this);

        closeBtn = new JButton("CLOSE");
        closeBtn.setBounds(490, 510, 80, 40);
        closeBtn.addActionListener(e -> frame.dispose());
        closeBtn.setBackground(Color.BLUE);
        closeBtn.setForeground(Color.WHITE);
        frame.add(closeBtn);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Fetch and display data for the current month and year initially
        selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
        selectedYear = Calendar.getInstance().get(Calendar.YEAR);
        fetchAndDisplayData();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(back)) {
            frame.dispose();
            new Frame3(username); // Pass username to the previous frame
        } else if (ae.getSource().equals(monthComboBox) || ae.getSource().equals(yearComboBox)) {
            selectedMonth = monthComboBox.getSelectedIndex() + 1; // Convert index to 1-based month number
            selectedYear = (Integer) yearComboBox.getSelectedItem(); // Get selected year
            fetchAndDisplayData();
        }
    }

    private void fetchAndDisplayData() {
        // Clear existing data from table model
        model.setRowCount(0);
    
        // Create query based on selected month and year, ordered by date
        String query = "SELECT TO_CHAR(expdate, 'YYYY-MM-DD') AS expdate, description, transaction_type, amount " 
                + "FROM expense " 
                + "WHERE EXTRACT(MONTH FROM expdate) = ? " 
                + "AND EXTRACT(YEAR FROM expdate) = ? "
                + "AND username = ? " // Add username filter
                + "ORDER BY expdate ASC";
    
        double totalAmount = 0;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "12345");
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, selectedMonth); // Set the selected month
            pstmt.setInt(2, selectedYear);  // Set the selected year
            pstmt.setString(3, username);  // Set the username
    
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("expdate");
                String descp = rs.getString("description");
                String transtype = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
    
                totalAmount += amount;
                model.addRow(new Object[]{date, descp, transtype, amount});
            }
    
            // Close the result set, statement, and connection
            rs.close();
            pstmt.close();
            con.close();
    
            model.addRow(new Object[]{"Total", "", "", totalAmount});
        } catch (Exception se) {
            se.printStackTrace();
        }
    }
}
