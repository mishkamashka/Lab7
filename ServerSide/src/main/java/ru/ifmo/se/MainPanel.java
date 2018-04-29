package ru.ifmo.se;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collection;

public class MainPanel extends JFrame {
    JMenu menu;
    JMenuBar jMenuBar;
    JMenuItem jMenuItem;
    JLabel label;
    JLabel resLabel;
    JTextField textField;
    JTree jTree;
    JButton addButton;
    JButton remButton;
    JPanel jPanel;
    Container container;
    DefaultTreeModel model;
    DefaultMutableTreeNode root;
    GroupLayout groupLayout;


    public MainPanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lab 7. ServerSide");
        createMenu();

        container = getContentPane();
        jPanel = new JPanel();
        groupLayout = new GroupLayout(jPanel);
        groupLayout.getAutoCreateGaps();
        container.add(jPanel);
        root = new DefaultMutableTreeNode("People");
        jTree = new JTree(root);
        updateTree();
        model = (DefaultTreeModel) jTree.getModel();
        createOptions();
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup()
                        .addComponent(jTree).addGap(100)
                        .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(label).addGap(10)
                        .addComponent(textField).addGap(10)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(addButton).addGap(10)
                                .addComponent(remButton)).addGap(10)
                        .addComponent(resLabel)));
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(jTree).addGap(100)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(label).addGap(10)
                        .addComponent(textField).addGap(10)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(addButton).addGap(10)
                                .addComponent(remButton)).addGap(10)
                        .addComponent(resLabel)));

        model.reload();
        groupLayout.linkSize(textField);
        jPanel.setLayout(groupLayout);
        pack();
        setVisible(true);
    }

    public void updateTree(){ //to google: how to update jtree
        root.removeAllChildren();
        Server.collec.forEach(person -> root.add(new DefaultMutableTreeNode(person.toString())));
        jTree.updateUI();
        jPanel.updateUI();
    }

    public void createMenu(){
        jMenuBar = new JMenuBar();
        menu = new JMenu("Menu");
        jMenuItem = new JMenuItem("Load collection from the file (Current collection will be lost)");
        jMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a) {
                Connection.filemaker();
                try{
                    Connection.clear();
                    Connection.load();
                } catch (IOException e){
                    e.printStackTrace();
                }
                updateTree();
            }
        });
        menu.add(jMenuItem);
        jMenuItem = new JMenuItem("Load current collection");
        jMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a) {
                updateTree();
            }
        });
        menu.add(jMenuItem);
        jMenuItem = new JMenuItem("Save current collection to the file");
        jMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a) {
                Connection.saveOnQuit();
            }
        });
        menu.add(jMenuItem);
        jMenuBar.add(menu);
        setJMenuBar(jMenuBar);
    }

    public void createOptions(){
        label = new JLabel("Object to add/Remove objects greater than:");
        resLabel = new JLabel();
        textField = new JTextField("{\"name\":\"Andy\"}",15);
        addButton = new JButton("Add object");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string = textField.getText();
                resLabel.setText(Connection.addObject(string));
                updateTree();
            }
        });
        remButton = new JButton("Remove greater objects");
        remButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string = textField.getText();
                resLabel.setText(Connection.removeGreater(string));
                updateTree();
            }
        });
    }
}
