import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frame1 implements ActionListener {
    JFrame frame;
    JButton addExpButton, viewExpButton;
    JLabel heading;
    String username;

    Frame1(String username) {
        this.username = username;
        frame = new JFrame("Expense Tracker");
        frame.setSize(500, 350);
        frame.setLocation(300, 300);
        frame.setLayout(null);

        heading = new JLabel("WELCOME TO EXPENSE TRACKER, " + username);
        heading.setBounds(50, 50, 400, 50);
        frame.add(heading);

        Font f = new Font("Arial", Font.BOLD, 18);
        heading.setFont(f);
        heading.setForeground(Color.BLUE);

        addExpButton = new JButton("ADD EXPENSE");
        addExpButton.setBounds(168, 140, 150, 40);
        addExpButton.setBackground(Color.BLUE);
        addExpButton.setForeground(Color.WHITE);
        frame.add(addExpButton);

        viewExpButton = new JButton("VIEW EXPENSE");
        viewExpButton.setBounds(168, 220, 150, 40);
        viewExpButton.setBackground(Color.BLUE);
        viewExpButton.setForeground(Color.WHITE);
        frame.add(viewExpButton);

        addExpButton.addActionListener(this);
        viewExpButton.addActionListener(this);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(addExpButton)) {
            frame.dispose();
            new Frame2(username); // Pass username to the next frame
        } else if (ae.getSource().equals(viewExpButton)) {
            frame.dispose();
            new Frame3(username); // Pass username to the next frame
        }
    }
}
