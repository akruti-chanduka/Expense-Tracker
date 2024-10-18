import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.Month;
import java.time.LocalDate;

public class Frame4 implements ActionListener {
    JFrame frame;
    JLabel heading;
    JButton back;
    JTable table;
    DefaultTableModel model;
    JScrollPane scrollPane;
    JButton closeBtn;
    JComboBox<String> monthDropdown;
    String username;

    Frame4(String username) {
        this.username = username;
        frame = new JFrame("Today's Expense");
        frame.setSize(500, 600);
        frame.setLocation(250, 150);
        frame.setLayout(null);

        heading = new JLabel("TODAY'S EXPENSE");
        heading.setBounds(20, 17, 320, 40);
        frame.add(heading);

        Font f = new Font("Arial", Font.BOLD, 18);
        heading.setFont(f);
        heading.setForeground(Color.BLUE);

        // Initialize JComboBox with month names
        

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
        fetchAndDisplayData();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(back)) {
            frame.dispose();
            new Frame3(username); // Pass username to the next frame
        }
    }

    private void fetchAndDisplayData() {
        String query = "SELECT TO_CHAR(expdate, 'YYYY-MM-DD') AS expdate, description, transaction_type, amount " +
                       "FROM expense WHERE TRUNC(expdate) = TRUNC(CURRENT_DATE) AND username = ?";

        double totalAmount = 0;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "12345");
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("expdate");
                String descp = rs.getString("description");
                String transactype = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");

                model.addRow(new Object[]{date, descp, transactype, amount});
                totalAmount += amount;
            }

            rs.close();
            stmt.close();
            con.close();

            model.addRow(new Object[]{"Total", "", "", totalAmount});
        } catch (Exception se) {
            se.printStackTrace();
        }
    }
}
