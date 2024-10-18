import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Frame2 implements ActionListener {
    JFrame frame;
    JLabel heading, ldate, ldescp, lamount;
    JTextField fdate, fdescp, famount;
    JButton add, back;
    DateFormat df;
    JFormattedTextField txtDate;
    JComboBox<String> c1;
    JRadioButton credit, debit;
    String username;

    Frame2(String username) {
        this.username = username;
        frame = new JFrame("Add Expense");
        frame.setSize(500, 350);
        frame.setLocation(300, 300);
        frame.setLayout(null);

        heading = new JLabel("ADD YOUR EXPENSE");
        heading.setBounds(20, 17, 400, 40);
        frame.add(heading);

        Font f = new Font("Arial", Font.BOLD, 18);
        heading.setFont(f);
        heading.setForeground(Color.BLUE);

        ldate = new JLabel("Date:(MM/dd/yyyy)");
        ldate.setBounds(20, 70, 80, 50);
        frame.add(ldate);
        Font f1 = new Font("Arial", Font.BOLD, 15);
        ldate.setFont(f1);

        ldescp = new JLabel("Description:");
        ldescp.setBounds(20, 120, 120, 50);
        frame.add(ldescp);
        Font f2 = new Font("Arial", Font.BOLD, 15);
        ldescp.setFont(f2);

        lamount = new JLabel("Amount:");
        lamount.setBounds(20, 170, 120, 50);
        frame.add(lamount);
        Font f3 = new Font("Arial", Font.BOLD, 15);
        lamount.setFont(f3);

        famount = new JTextField();
        famount.setBounds(170, 180, 250, 30);
        frame.add(famount);

        credit = new JRadioButton("Credit");
        credit.setBounds(170, 210, 80, 50);
        frame.add(credit);

        debit = new JRadioButton("Debit");
        debit.setBounds(260, 210, 80, 50);
        frame.add(debit);

        ButtonGroup bg = new ButtonGroup();
        bg.add(credit);
        bg.add(debit);

        add = new JButton("ADD");
        add.setBounds(170, 260, 70, 40);
        add.setBackground(Color.BLUE);
        add.setForeground(Color.WHITE);
        frame.add(add);

        back = new JButton("BACK");
        back.setBounds(350, 260, 70, 40);
        back.setBackground(Color.BLUE);
        back.setForeground(Color.WHITE);
        frame.add(back);

        df = new SimpleDateFormat("MM/dd/yyyy");
        txtDate = new JFormattedTextField(df);
        txtDate.setBounds(170, 80, 250, 30);
        txtDate.setValue(new Date());
        frame.add(txtDate);

        String s1[] = { "Salary", "Gift", "Shopping", "Bill", "Food & Drink", "Transportation", "Entertainment", "Others" };
        c1 = new JComboBox<String>(s1);
        c1.setBounds(170, 130, 250, 30);
        frame.add(c1);

        back.addActionListener(this);
        add.addActionListener(this);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        String transactype = "";

        try {
            if (ae.getSource().equals(back)) {
                frame.dispose();
                new Frame1(username); // Pass username to the next frame
            } else if (ae.getSource().equals(add)) {
                String expdate = txtDate.getText();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                java.util.Date date = dateFormat.parse(expdate);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                String descp = (String) c1.getSelectedItem();
                double amount = Double.parseDouble(famount.getText());

                if (credit.isSelected()) {
                    transactype = "credit";
                } else if (debit.isSelected()) {
                    transactype = "debit";
                    amount *= -1;
                }

                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "12345");

                String sql = "INSERT INTO expense (username, expdate, description, transaction_type, amount) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, username);
                statement.setDate(2, sqlDate);
                statement.setString(3, descp);
                statement.setString(4, transactype);
                statement.setDouble(5, amount);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(frame, "Data saved successfully!");
                    famount.setText("");
                }

                statement.close();
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "ERROR!!!!!");
        }
    }
}
