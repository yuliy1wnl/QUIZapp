
package quiz.application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Rules_page extends JFrame implements ActionListener{
      JButton back,start;
     String name;
    Rules_page(String name){
        
       this.name = name;
        
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        
        
        JLabel heading = new JLabel("Welcome "+name+" To Mini Quiz");
        heading.setBounds(180,20,700,30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);
        add(heading);
        
        JLabel rule = new JLabel("Welcome "+name+" To Mini Quiz");
        rule.setBounds(20,90,700,350);
        rule.setFont(new Font("Tahoma", Font.PLAIN, 16));
        rule.setText(
                "<html>"+ 
                "1. 1 marks will be awarded for correct answer" + "<br><br>" +
                "2. The quizzes consists of questions based on JAVA" + "<br><br>" +
                "3. After responding to a question, click on the \"Next Question\" button at the bottom to go to the next question" + "<br><br>" +
                "4. Response time for each question is 30 seconds." + "<br><br>" +
                "5. Each question in the quiz is of multiple-choice " + "<br><br>" +
                "6. Read each question carefully, Submit button will be availble at the last question" + "<br><br>" +
                 
                  " Good-Luck "+
            "<html>"
        );
        rule.setForeground(Color.WHITE);
        add(rule);
        
        
        
         back = new JButton("Back");
        back.setBounds(250,500,100,30);
        back.setBackground(new Color(30,144,254));
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);
        
         start = new JButton("Start");
        start.setBounds(400,500,100,30);
        start.setBackground(new Color(30,144,254));
        start.setForeground(Color.WHITE);
        start.addActionListener(this);
        add(start);
        
        
        
        
         setSize(800,650);
        setLocation(350, 100);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==start){
            setVisible(false);
            new Questions(name);
            
        }else {
            setVisible(false);
            new Login();
        }
    }
    
    
    
    public static void main(String[] args) {
        new Rules_page("User");
    }
}