import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Frame3 implements ActionListener {
    JFrame frame;
    JLabel heading;
    JButton todays, monthly, total, back;
    String username;

    Frame3(String username) {
        this.username = username;
        frame = new JFrame("View Expense");
        frame.setSize(500, 350);
        frame.setLocation(300, 300);
        frame.setLayout(null);

        heading = new JLabel("VIEW YOUR EXPENSE");
        heading.setBounds(40, 17, 400, 40);
        frame.add(heading);

        Font f = new Font("Arial", Font.BOLD, 18);
        heading.setFont(f);
        heading.setForeground(Color.BLUE);

        todays = new JButton("TODAY'S");
        todays.setBounds(40, 120, 110, 40);
        todays.setBackground(Color.BLUE);
        todays.setForeground(Color.WHITE);
        frame.add(todays);

        monthly = new JButton("MONTHLY");
        monthly.setBounds(180, 120, 110, 40);
        monthly.setBackground(Color.BLUE);
        monthly.setForeground(Color.WHITE);
        frame.add(monthly);

        total = new JButton("TOTAL");
        total.setBounds(320, 120, 110, 40);
        total.setBackground(Color.BLUE);
        total.setForeground(Color.WHITE);
        frame.add(total);

        back = new JButton("BACK");
        back.setBounds(358, 230, 70, 40);
        back.setBackground(Color.BLUE);
        back.setForeground(Color.WHITE);
        frame.add(back);

        todays.addActionListener(this);
        monthly.addActionListener(this);
        total.addActionListener(this);
        back.addActionListener(this);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(back)) {
            frame.dispose();
            new Frame1(username); // Pass username to the next frame
        } else if (ae.getSource().equals(todays)) {
            frame.dispose();
            new Frame4(username); // Pass username to the next frame
        } else if (ae.getSource().equals(monthly)) {
            frame.dispose();
            new Frame5(username); // Implement this similarly
        } else if (ae.getSource().equals(total)) {
            frame.dispose();
            new Frame6(username); // Implement this similarly
        }
    }
}
