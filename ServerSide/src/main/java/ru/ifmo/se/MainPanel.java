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

public class MainPanel {
    static JMenu menu;
    static JLabel selectedLabel;
    static JFrame f;
    static JMenuBar jMenuBar;
    static JMenuItem jMenuItem;
    static CollectionPanel collectionPanel;
    static JTree jTree;


    public static void main (String ... args){

        /*Known kozlik = new Known("Andy");
        Known neznaika = new Known("Nikken");
        Known stranger = new Known("Frank");
        Server.collec.add(kozlik);
        Server.collec.add(neznaika);
        Server.collec.add(stranger);*/

        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Lab 7. ServerSide");
        //f.setLayout(new BorderLayout());

        MainPanel.updateTree();

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
                MainPanel.updateTree();
            }
        });
        menu.add(jMenuItem);
        jMenuBar.add(menu);
        f.setJMenuBar(jMenuBar);
        f.pack();
        f.setVisible(true);
    }

    public static void updateTree(){
        collectionPanel = new CollectionPanel(Server.collec);
        jTree = collectionPanel.getJTree();
        f.add(jTree, BorderLayout.CENTER);
        f.add(new JScrollPane(jTree));
        selectedLabel = new JLabel();
        f.add(selectedLabel, BorderLayout.SOUTH);
        jTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) collectionPanel.getJTree().getLastSelectedPathComponent();
                try {
                    Known known = new Known(selectedNode.getUserObject().toString());
                    for (Person person : Server.collec) {
                        if (person.equals(known))
                            selectedLabel.setText(person.description());
                    }
                } catch (NullPointerException ee){ }
            }
        });
    }
}
