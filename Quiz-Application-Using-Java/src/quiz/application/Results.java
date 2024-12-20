package quiz.application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Results extends JFrame implements ActionListener {

    String name;
    String id;
    int contact;
    int score;

    Results(String name, String id, int contact, int score) {
        this.name = name;
        this.id = id;
        this.contact = contact;
        this.score = score;

        setBounds(290, 120, 850, 550);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("icons/sc.jpg"));
        Image img2 = img1.getImage().getScaledInstance(300, 250, Image.SCALE_DEFAULT);
        ImageIcon img3 = new ImageIcon(img2);
        JLabel image = new JLabel(img3);
        image.setBounds(0, 100, 300, 250);
        add(image);

        JLabel heading = new JLabel("Thank you, " + name + ", for Playing Mini Quiz");
        heading.setBounds(220, 30, 700, 30);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 26));
        heading.setForeground(Color.RED);
        add(heading);

        JLabel lblscore = new JLabel("Your Score is " + score);
        lblscore.setBounds(490, 200, 300, 30);
        lblscore.setFont(new Font("Tahoma", Font.PLAIN, 26));
        lblscore.setForeground(Color.RED);
        add(lblscore);

        JButton playAgain = new JButton("Play Again");
        playAgain.setBounds(400, 270, 120, 30);
        playAgain.setFont(new Font("Tahoma", Font.PLAIN, 16));
        playAgain.setBackground(new Color(30, 144, 254));
        playAgain.setForeground(Color.WHITE);
        playAgain.addActionListener(this);
        add(playAgain);

        JButton leaderboard = new JButton("Leaderboard");
        leaderboard.setBounds(540, 270, 140, 30);
        leaderboard.setFont(new Font("Tahoma", Font.PLAIN, 16));
        leaderboard.setBackground(new Color(30, 144, 254));
        leaderboard.setForeground(Color.WHITE);
        leaderboard.addActionListener(e -> viewLeaderboard());
        add(leaderboard);

        saveScore(name, id, contact, score);

        setVisible(true);
    }

    private void saveScore(String name, String id,int contact, int score) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizdb", "root", "yuliy1wnl")) {
    
    String checkQuery = "SELECT * FROM scores WHERE id = ?";
    try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
        checkStmt.setString(1, id);
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next()) {
                
                int existingScore = rs.getInt("score");
                if (score > existingScore) {
                    String updateQuery = "UPDATE scores SET score = ? WHERE id = ? name = ? AND contact =?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, score); 
                        updateStmt.setString(2, id);
                        updateStmt.setString(3, name);
                        updateStmt.setInt(4, contact);
                        updateStmt.executeUpdate();
                    }
                }
            } else {
                
                String insertQuery = "INSERT INTO scores (name, id, contact, score) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, id);
                    insertStmt.setInt(3, contact);
                    insertStmt.setInt(4, score);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
} catch (SQLException e) {
    e.printStackTrace();
}

    }

    private void viewLeaderboard() {
        JFrame leaderboardFrame = new JFrame("Leaderboard");
        leaderboardFrame.setBounds(290, 120, 400, 300);
        leaderboardFrame.getContentPane().setBackground(Color.WHITE);
        leaderboardFrame.setLayout(null);

        JLabel title = new JLabel("LEADERBOARD");
        title.setBounds(120, 10, 200, 30);
        title.setFont(new Font("Tahoma", Font.BOLD, 20));
        title.setForeground(Color.BLUE);
        leaderboardFrame.add(title);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizdb", "root", "yuliy1wnl");
            String query = "SELECT name, id, score FROM scores ORDER BY score DESC LIMIT 3";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int y = 60;
            while (rs.next()) {
                String playerName = rs.getString("name");
                String playerId = rs.getString("id");
                int playerScore = rs.getInt("score");

                JLabel scoreLabel = new JLabel(playerName + " (" + playerId + ") - " + playerScore);
                scoreLabel.setBounds(100, y, 300, 30);
                scoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
                scoreLabel.setForeground(Color.BLACK);
                leaderboardFrame.add(scoreLabel);

                y += 40;
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        leaderboardFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Login(); 
    }

    public static void main(String[] args) {
        new Results("User", "1",0 , 0);
    }
}