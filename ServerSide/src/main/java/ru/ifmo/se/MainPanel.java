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
    JTree jTree;
    JPanel jPanel;
    Container container;
    DefaultTreeModel model;
    DefaultMutableTreeNode root;


    public MainPanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lab 7. ServerSide");
        createMenu();

        container = getContentPane();
        jPanel = new JPanel(new BorderLayout());
        add(jPanel);
        container.add(jPanel);
        root = new DefaultMutableTreeNode("People");
        jTree = new JTree(root);
        updateTree();
        jPanel.add(new JScrollPane(jTree));
        model = (DefaultTreeModel) jTree.getModel();
        jPanel.add(jTree, BorderLayout.CENTER);
        model.reload();
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
}
