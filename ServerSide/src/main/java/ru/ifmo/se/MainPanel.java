package ru.ifmo.se;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainPanel extends JFrame {
    JMenu menu;
    JLabel selectedLabel;
    JMenuBar jMenuBar;
    JMenuItem jMenuItem;
    CollectionPanel collectionPanel;
    JTree jTree;
    JPanel jPanel;


    public MainPanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lab 7. ServerSide");
        createMenu();

        Container container = getContentPane();
        jPanel = new JPanel(new BorderLayout());
        jPanel.setDoubleBuffered(true);
        container.add(jPanel);
        updateTree();
        pack();
        setVisible(true);
    }

    public void updateTree(){
        collectionPanel = new CollectionPanel(Server.collec);
        jTree = collectionPanel.getJTree();
        jPanel.add(jTree, BorderLayout.CENTER);
        jPanel.add(new JScrollPane(jTree));
        selectedLabel = new JLabel();
        jPanel.add(selectedLabel, BorderLayout.SOUTH);
        jTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                try {
                    Known known = new Known(selectedNode.getUserObject().toString());
                    for (Person person : Server.collec) {
                        if (person.equals(known))
                            selectedLabel.setText(person.description());
                    }
                } catch (NullPointerException ee){ }
            }
        });
        jPanel.setVisible(true);
    }

    public void createMenu(){
        jMenuBar = new JMenuBar();
        menu = new JMenu("Menu");
        jMenuItem = new JMenuItem("Load collection from the file",
                KeyEvent.VK_1);
        jMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a) {
                //f.setVisible(false);
                //f.remove(jTree);
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
        jMenuBar.add(menu);
        setJMenuBar(jMenuBar);
    }
}
