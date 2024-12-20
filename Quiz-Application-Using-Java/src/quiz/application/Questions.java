package quiz.application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
public class Questions extends JFrame implements ActionListener {
    String[][] questions;
    String[][] answers;
    String[][] userAns = new String[10][1];

    JLabel qno, ques;
    JRadioButton opt1, opt2, opt3, opt4;
    ButtonGroup groupoptions;
    JButton next, submit, lifeline;

    public static int timer = 30;
    public static int ans_given = 0;
    public static int count = 0;
    public static int score = 0;

    String name;
    String id;
    int contact;
    

    Questions(String name) {
        this.name = name;
        this.id = id;
        this.contact = contact;

        setBounds(0, 0, 1370, 730);
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("icons/quiz.jpg"));
        JLabel image = new JLabel(img1);
        image.setBounds(0, 0, 1440, 350);
        add(image);

        qno = new JLabel();
        qno.setBounds(130, 400, 50, 30);
        qno.setForeground(Color.WHITE);
        qno.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(qno);

        ques = new JLabel();
        ques.setBounds(180, 400, 900, 30);
        ques.setForeground(Color.WHITE);
        ques.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(ques);

        opt1 = new JRadioButton();
        opt1.setBounds(170, 450, 700, 30);
        opt1.setBackground(Color.BLACK);
        opt1.setForeground(Color.WHITE);
        opt1.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt1);

        opt2 = new JRadioButton();
        opt2.setBounds(170, 490, 700, 30);
        opt2.setBackground(Color.BLACK);
        opt2.setForeground(Color.WHITE);
        opt2.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt2);

        opt3 = new JRadioButton();
        opt3.setBounds(170, 530, 700, 30);
        opt3.setBackground(Color.BLACK);
        opt3.setForeground(Color.WHITE);
        opt3.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt3);

        opt4 = new JRadioButton();
        opt4.setBounds(170, 570, 700, 30);
        opt4.setBackground(Color.BLACK);
        opt4.setForeground(Color.WHITE);
        opt4.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt4);
        
      

        groupoptions = new ButtonGroup();
        groupoptions.add(opt1);
        groupoptions.add(opt2);
        groupoptions.add(opt3);
        groupoptions.add(opt4);

        
        next = new JButton("Next");
        next.setBounds(1100, 470, 200, 40);
        next.setFont(new Font("Tahoma", Font.PLAIN, 22));
        next.setBackground(new Color(30, 144, 254));
        next.setForeground(Color.WHITE);
        next.addActionListener(this);
        add(next);

       

        submit = new JButton("Submit");
        submit.setBounds(1100, 530, 200, 40);
        submit.setFont(new Font("Tahoma", Font.PLAIN, 22));
        submit.setBackground(new Color(30, 144, 254));
        submit.setForeground(Color.WHITE);
        submit.setEnabled(false);
        submit.addActionListener(this);
        add(submit);

        fetchQuestionsFromDatabase();
        start(count);

        setVisible(true);
    }

    private void fetchQuestionsFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizdb", "root", "yuliy1wnl");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM questions LIMIT 10");

            questions = new String[10][5];
            answers = new String[10][2];

            int index = 0;
            while (resultSet.next()) {
                questions[index][0] = resultSet.getString("question");
                questions[index][1] = resultSet.getString("option1");
                questions[index][2] = resultSet.getString("option2");
                questions[index][3] = resultSet.getString("option3");
                questions[index][4] = resultSet.getString("option4");

                answers[index][1] = resultSet.getString("correct_option");
                index++;
            }
            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load questions from database: " + e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()== next){
            repaint();
            opt1.setEnabled(true);
            opt2.setEnabled(true);
            opt3.setEnabled(true);
            opt4.setEnabled(true);
            
            ans_given=1;
            
            
            if(groupoptions.getSelection()==null){
                userAns[count][0] = "";
            }else{
                userAns[count][0] = groupoptions.getSelection().getActionCommand();
            }
               
            if(count == 8){
                next.setEnabled(false);
                submit.setEnabled(true);
            }
            count++;
            start(count);
            
            
            }else if(ae.getSource()== lifeline){
            if(count == 2 || count == 4 || count == 6 || count == 8|| count == 9 ){
                opt2.setEnabled(false);
                opt3.setEnabled(false);
            }else{
                opt1.setEnabled(false);
                opt4.setEnabled(false);
            }
            lifeline.setEnabled(false);
            
            }else if(ae.getSource()== submit){
                ans_given=1;
               if(groupoptions.getSelection()==null){
                userAns[count][0] = "";
            }else{
                userAns[count][0] = groupoptions.getSelection().getActionCommand();
            }
   
          
                  
            for(int i=0; i<userAns.length; i++){
                if(userAns[i][0].equals(answers[i][1])){
                     score += 1;
                }else{
                    score += 0;
                }   
            }  
               
            setVisible(false);
            new Results(name,id,contact,score);
            
        }
    }
    
    public void paint(Graphics g){
        super.paint(g);
        
        String time = "Time Left -  " + timer + " Seconds";
        g.setColor(Color.BLUE);
        g.setFont(new Font("Tahoma",Font.PLAIN,20));
        
        if(timer>0){
            g.drawString(time, 1100, 420);
        }else{
             g.drawString("Time Over!!", 1100, 420);
        }
        timer--;
        
        try {
            Thread.sleep(1000);
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        if(ans_given ==1){
            ans_given=0;
            timer =30;
        }else if(timer < 0){
            timer = 30;
            opt1.setEnabled(true);
            opt2.setEnabled(true);
            opt3.setEnabled(true);
            opt4.setEnabled(true);
            
            if(count == 8){
                next.setEnabled(false);
                submit.setEnabled(true);
            }
            
            if(count==9){
                if(groupoptions.getSelection()==null){
                userAns[count][0] = "";
            }else{
                userAns[count][0] = groupoptions.getSelection().getActionCommand();
            }
                  
               for(int i=0; i<userAns.length; i++){
                   if(userAns[i][0].equals(answers[i][1])){
                    score += 1;
               }else{
                    score += 0;
                   }
               }  
                 setVisible(false);
                new Results(name,id,contact,score);
                  
            }else{    //next button
                 if(groupoptions.getSelection()==null){
                userAns[count][0] = "";
            }else{
                userAns[count][0] = groupoptions.getSelection().getActionCommand();
            }
            
            count++;
            start(count); 
            }
            
          
        }
    }

    public void start(int count) {
        qno.setText(" " + (count + 1) + ". ");
        ques.setText(questions[count][0]);

        opt1.setText(questions[count][1]);
        opt1.setActionCommand(questions[count][1]);

        opt2.setText(questions[count][2]);
        opt2.setActionCommand(questions[count][2]);

        opt3.setText(questions[count][3]);
        opt3.setActionCommand(questions[count][3]);

        opt4.setText(questions[count][4]);
        opt4.setActionCommand(questions[count][4]);

        groupoptions.clearSelection();
    }

    public static void main(String[] args) {
        new Questions("User");
    }
}
