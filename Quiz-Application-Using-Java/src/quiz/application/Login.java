package quiz.application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JButton rules, back;
    JTextField tfName, tfID, tfContact;

    Login() {
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        
        ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("icons/quizt.png"));
        JLabel image = new JLabel(img1);
        image.setBounds(0,0,500,500);
        add(image);

        JLabel heading = new JLabel("Mini Quiz");
        heading.setBounds(650, 60, 300, 45);
        heading.setFont(new Font("Tahoma", Font.BOLD, 40));
        heading.setForeground(new Color(30, 144, 254));
        add(heading);

        JLabel lblName = new JLabel("Enter Your Name");
        lblName.setBounds(600, 150, 300, 20);
        lblName.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblName.setForeground(Color.WHITE);
        add(lblName);

        tfName = new JTextField();
        tfName.setBounds(600, 180, 300, 25);
        tfName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(tfName);

        JLabel lblID = new JLabel("Enter Your ID");
        lblID.setBounds(600, 220, 300, 20);
        lblID.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblID.setForeground(Color.WHITE);
        add(lblID);

        tfID = new JTextField();
        tfID.setBounds(600, 250, 300, 25);
        tfID.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(tfID);

        JLabel lblContact = new JLabel("Enter Your Contact No.");
        lblContact.setBounds(600, 290, 300, 20);
        lblContact.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblContact.setForeground(Color.WHITE);
        add(lblContact);

        tfContact = new JTextField();
        tfContact.setBounds(600, 320, 300, 25);
        tfContact.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(tfContact);

        rules = new JButton("Go");
        rules.setBounds(600, 380, 120, 25);
        rules.setBackground(new Color(30, 144, 254));
        rules.setForeground(Color.WHITE);
        rules.addActionListener(this);
        add(rules);

        back = new JButton("Back");
        back.setBounds(780, 380, 120, 25);
        back.setBackground(new Color(30, 144, 254));
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        setSize(1000, 500);
        setLocation(200, 150);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == rules) {
            String name = tfName.getText();
            String id = tfID.getText();
            String contact = tfContact.getText();

            if (name.isEmpty() || id.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are mandatory!");
                return;
            }

            if (!contact.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Contact number must be 10 digits!");
                return;
            }

            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizdb", "root", "yuliy1wnl");
                PreparedStatement checkUser = conn.prepareStatement(
                    "SELECT COUNT(*) FROM scores WHERE id = ? OR contact = ?"
                );
                checkUser.setString(1, id);
                checkUser.setString(2, contact);

                ResultSet rs = checkUser.executeQuery();
                rs.next();

                if (rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "ID or Contact already exists!");
                } else {
                    PreparedStatement insertUser = conn.prepareStatement(
                        "INSERT INTO scores (id, name, contact, score) VALUES (?, ?, ?, 0)"
                    );
                    insertUser.setString(1, id);
                    insertUser.setString(2, name);
                    insertUser.setString(3, contact);
                    insertUser.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    setVisible(false);
                    new Rules_page(name);
                }

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new Login();    
    }
}
