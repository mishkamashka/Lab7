package ru.ifmo.se;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.locks.ReentrantLock;

public class AuthPanel extends JFrame {
    ClientApp app;
    JButton okButton;
    JButton cancelButton;
    JLabel label;
    JLabel serverAnsw;
    JPasswordField textField;
    Container container;
    JPanel jPanel;
    ReentrantLock lock = new ReentrantLock();
    GroupLayout groupLayout;

    public AuthPanel(ClientApp app){
        super("Authorisation");
        this.app = app;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        container = getContentPane();
        jPanel = new JPanel();
        groupLayout = new GroupLayout(jPanel);
        groupLayout.getAutoCreateGaps();
        container.add(jPanel);

        label = new JLabel("Enter password:");
        serverAnsw = new JLabel("Connecting to server...");
        textField = new JPasswordField();
        textField.setEchoChar('☀');
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    char[] chars;
                    chars = textField.getPassword();
                    String password = new String(chars);
                    try{
                        ClientApp.toServer.println(password);
                    } catch (NullPointerException ee){

                    }
                    String answer = app.gettingResponse();
                    if (answer.startsWith("You've")) {
                        setVisible(false);
                        MainPanel.isAuthorized = true;
                    }
                    else
                        serverAnsw.setText(answer);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] chars;
                chars = textField.getPassword();
                String password = new String(chars);
                try{
                    ClientApp.toServer.println(password);
                } catch (NullPointerException ee){

                }
                String answer = app.gettingResponse();
                if (answer.startsWith("You've")) {
                    setVisible(false);
                    MainPanel.isAuthorized = true;
                }
                else
                    serverAnsw.setText(answer);
            }
        });
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        group();
        jPanel.setLayout(groupLayout);
        groupLayout.linkSize(textField);
        pack();
        setVisible(true);
    }

    public void group(){
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(label).addGap(10)
                        .addComponent(textField,20,20,20).addGap(10)
                        .addGroup(groupLayout.createParallelGroup()
                            .addComponent(okButton).addGap(10)
                            .addComponent(cancelButton)).addGap(10)
                        .addComponent(serverAnsw));
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup()
                        .addComponent(label).addGap(10)
                        .addComponent(textField,100,150,150).addGap(10)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(okButton).addGap(10)
                                .addComponent(cancelButton)).addGap(10)
                        .addComponent(serverAnsw,200,250,300));
    }

    public void setLabel(String string){
        serverAnsw.setText(string);
    }
}
