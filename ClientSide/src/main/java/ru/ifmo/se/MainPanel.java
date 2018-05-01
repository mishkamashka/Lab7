package ru.ifmo.se;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.nio.ByteOrder;

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
    JButton repaintButton;
    JPanel jPanel;
    Container container;
    DefaultTreeModel model;
    DefaultMutableTreeNode root;
    GroupLayout groupLayout;
    ClientApp app;
    GraphPanel graphPanel;


    public MainPanel() {
        app = new ClientApp();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lab 7. ClientSide");
        setResizable(false);
        createMenu();
        container = getContentPane();
        jPanel = new JPanel();
        groupLayout = new GroupLayout(jPanel);
        groupLayout.getAutoCreateGaps();
        container.add(jPanel);
        root = new DefaultMutableTreeNode("People");
        jTree = new JTree(root);
        app.connect();
        ClientApp.toServer.println("data_request");
        app.clear();
        app.load();
        updateTree();

        graphPanel = new GraphPanel(app);
        //container.add(graphPanel);

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
                        .addComponent(resLabel))
                        .addComponent(repaintButton).addGap(10)
                        .addComponent(graphPanel, 300,500,500));
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(jTree).addGap(100)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(label).addGap(10)
                        .addComponent(textField).addGap(10)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(addButton).addGap(10)
                                .addComponent(remButton)).addGap(10)
                        .addComponent(resLabel)).addGap(50)
                        .addComponent(repaintButton).addGap(10)
                        .addComponent(graphPanel, 300, 500, 500));
        model.reload();
        groupLayout.linkSize(textField);
        jPanel.setLayout(groupLayout);

        //adding graph-thing

        pack();
        setVisible(true);
    }

    public void updateTree(){
        root.removeAllChildren();
        app.collec.forEach(person -> root.add(new DefaultMutableTreeNode(person.toString())));
        jTree.updateUI();
        jPanel.updateUI();
    }

    public void createMenu(){
        jMenuBar = new JMenuBar();
        menu = new JMenu("Menu");
        jMenuItem = new JMenuItem("Load collection from server");
        jMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a) {
                ClientApp.toServer.println("data_request");
                app.clear();
                app.load();
                updateTree();
            }
        });
        menu.add(jMenuItem);
        jMenuItem = new JMenuItem("Save current collection on server");
        jMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a) {
                ClientApp.toServer.println("save");
                app.giveCollection();
                resLabel.setText(app.gettingResponse());
            }
        });
        menu.add(jMenuItem);
        jMenuItem = new JMenuItem("Clear current collection");
        jMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a) {
                app.clear();
                updateTree();
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
                resLabel.setText(app.addObject(string));
                updateTree();
            }
        });
        remButton = new JButton("Remove greater objects");
        remButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string = textField.getText();
                resLabel.setText(app.removeGreater(string));
                updateTree();
            }
        });
        repaintButton = new JButton("Repaint");
        repaintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // change only colors from chosen persons, all ellipses will be redrawn

                for (int i = 0; i < 255; i++){
                    makeBrighter();
                    try{
                        Thread.sleep(10);
                    }catch (InterruptedException ee){
                        ee.printStackTrace();
                    }
                }

                /*int i;
                while (true){
                    i = makeBrighter();
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException ee){
                        ee.printStackTrace();
                    }
                }*/

               /* makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();
                makeBrighter();*/



                //while (i != app.collec.size()*3){
            }
        });
    }

    public int makeBrighter(){
        int r;
        int g;
        int b;
        int i = 0;
        for (Person person: app.collec) {
            r = person.getColor().getRed();
            g = person.getColor().getGreen();
            b = person.getColor().getBlue();
            if (r < 255)
                r++;
            else i++;
            if (g < 255)
                g++;
            else i++;
            if (b < 255)
                b++;
            else i++;
            person.setColor(new Color(r,g,b));
        }
        graphPanel.repaint();
        return i;
    }
}
