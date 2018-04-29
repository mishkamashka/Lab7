package ru.ifmo.se;

        import javax.swing.*;
        import javax.swing.tree.DefaultMutableTreeNode;
        import java.awt.*;
        import java.util.Collections;
        import java.util.SortedSet;
        import java.util.TreeSet;

public class DataPanel {

}

class CollectionPanel extends JPanel{
    private JTree jTree = new JTree();
    SortedSet<Person> collec = Collections.synchronizedSortedSet(new TreeSet<Person>());

    CollectionPanel(SortedSet <Person> sortedSet){
        this.collec = sortedSet;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode peopleNode = new DefaultMutableTreeNode("People");
        root.add(peopleNode);
        Server.collec.forEach(person -> peopleNode.add(new DefaultMutableTreeNode(person.toString())));
        jTree = new JTree(root);
        add(jTree, BorderLayout.CENTER);
    }

    public JTree getJTree(){
        return jTree;
    }

}