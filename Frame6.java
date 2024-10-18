import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Frame6 implements ActionListener {
    JFrame frame;
    JLabel heading;
    JButton back;
    JTable table;
    DefaultTableModel model;
    JScrollPane scrollPane;
    JButton closeBtn;
    String username;

    Frame6(String username) {
        this.username = username; // Store the username

        frame = new JFrame("Total Expense");
        frame.setSize(500, 600);
        frame.setLocation(250, 150);
        frame.setLayout(null);

        heading = new JLabel("TOTAL EXPENSE");
        heading.setBounds(20, 17, 320, 40);
        frame.add(heading);

        Font f = new Font("Arial", Font.BOLD, 18);
        heading.setFont(f);
        heading.setForeground(Color.BLUE);

        model = new DefaultTableModel();
        String[] colName = {"Date", "Description", "Transaction_Type", "Amount"};
        model.setColumnIdentifiers(colName);

        table = new JTable();
        table.setModel(model);

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 60, 450, 400);
        frame.add(scrollPane);

        back = new JButton("BACK");
        back.setBounds(20, 480, 70, 40);
        back.setBackground(Color.BLUE);
        back.setForeground(Color.WHITE);
        frame.add(back);
        back.addActionListener(this);

        closeBtn = new JButton("CLOSE");
        closeBtn.setBounds(390, 480, 80, 40);
        closeBtn.addActionListener(e -> frame.dispose());
        closeBtn.setBackground(Color.BLUE);
        closeBtn.setForeground(Color.WHITE);
        frame.add(closeBtn);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Fetch and display data for the specified user
        fetchAndDisplayData();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(back)) {
            frame.dispose();
            new Frame3(username); // Pass username to the previous frame
        }
    }

    private void fetchAndDisplayData() {
        // Query to select all expenses for the current user
        String query = "SELECT TO_CHAR(expdate, 'YYYY-MM-DD') AS expdate, description, transaction_type, amount " +
                       "FROM expense " +
                       "WHERE username = ? " + // Add username filter
                       "ORDER BY expdate ASC";

        double totalAmount = 0;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "12345");
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, username); // Set the username

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("expdate");
                String descp = rs.getString("description");
                String transactype = rs.getString("transaction_type");
                double amount = rs.getDouble("amount"); // Corrected to getDouble for amount

                totalAmount += amount;
                model.addRow(new Object[]{date, descp, transactype, amount});
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
