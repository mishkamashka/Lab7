package ru.ifmo.se;

import ru.ifmo.se.enums.State;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.nio.ByteOrder;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class MainPanel extends JFrame {
    JMenu menu;
    JMenuBar jMenuBar;
    JMenuItem jMenuItem;
    JLabel label;
    JLabel resLabel;
    JLabel ageLabel;
    JLabel distLabel;
    JTextField textField;
    JFormattedTextField formattedTextField;
    JTree jTree;
    JButton addButton;
    JButton remButton;
    JButton startButton;
    JButton stopButton;
    JCheckBox checkBoxN;
    JCheckBox checkBoxA;
    JCheckBox checkBoxI;
    JCheckBox checkBoxB;
    JSlider slider;
    JPanel jPanel;
    Container container;
    DefaultTreeModel model;
    DefaultMutableTreeNode root;
    GroupLayout groupLayout;
    ClientApp app;
    GraphPanel graphPanel;
    Thread thread;
    Set<State> states = new HashSet<>();
    volatile Set<Person> persons = new HashSet<>();
    int age;
    long distance;
    volatile static boolean isAuthorized = false;

    public MainPanel() {
        app = new ClientApp();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lab 7. ClientSide");
        setResizable(false);
        createMenu();
        container = getContentPane();
        jPanel = new JPanel();
        groupLayout = new GroupLayout(jPanel);
        container.add(jPanel);
        root = new DefaultMutableTreeNode("People");
        jTree = new JTree(root);
        app.connect();
        while (!isAuthorized) {
        }
        ClientApp.toServer.println("data_request");
        app.clear();
        app.load();
        updateTree();

        graphPanel = new GraphPanel(app);
        graphPanel.addMouseListener(new MouseListener() {
                                        @Override
                                        public void mouseClicked(MouseEvent e) {
                                            int x = e.getX();
                                            int y = e.getY();
                                            int personX;
                                            int personY;
                                            Person person;
                                            for (Map.Entry<Person, Ellipse2D> entry: graphPanel.ellipsMap.entrySet()){
                                                personX = entry.getKey().getX();
                                                personY = entry.getKey().getY();
                                                if ((personX <= x && x <= (personX + 40))&&(personY <= y && y <=(personY + 20)))
                                                    graphPanel.setToolTipText(entry.getKey().toString());
                                            }
                                        }

                                        @Override
                                        public void mousePressed(MouseEvent e) {

                                        }

                                        @Override
                                        public void mouseReleased(MouseEvent e) {

                                        }

                                        @Override
                                        public void mouseEntered(MouseEvent e) {

                                        }

                                        @Override
                                        public void mouseExited(MouseEvent e) {

                                        }
                                    });
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
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(startButton).addGap(10)
                            .addComponent(stopButton).addGap(10)
                            .addComponent(checkBoxN).addGap(5)
                            .addComponent(checkBoxA).addGap(5)
                            .addComponent(checkBoxB).addGap(5)
                            .addComponent(checkBoxI).addGap(10)
                            .addComponent(ageLabel).addGap(10)
                            .addComponent(slider).addGap(10)
                            .addComponent(distLabel).addGap(10)
                            .addComponent(formattedTextField,20,20,20)).addGap(10)
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
                            .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(startButton).addGap(10)
                                .addComponent(stopButton).addGap(10)
                                .addComponent(checkBoxN).addGap(10)
                                .addComponent(checkBoxA).addGap(10)
                                .addComponent(checkBoxB).addGap(10)
                                .addComponent(checkBoxI).addGap(10)
                                .addComponent(ageLabel).addGap(10)
                                .addComponent(slider).addGap(10)
                                .addComponent(distLabel).addGap(10)
                                .addComponent(formattedTextField, 100,120,120)).addGap(10)
                            .addComponent(graphPanel, 300, 500, 500));
        model.reload();
        groupLayout.linkSize(textField);
        jPanel.setLayout(groupLayout);
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
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread = new Thread(() -> {
                    if (formattedTextField.getValue() != null)
                        distance = (Long) formattedTextField.getValue();
                    else distance = 20000;
                    int i = 0;
                    app.collec.forEach(person -> {
                        for (State state: states){
                            if (person.getState().equals(state) && (person.getAge() >= age) && (person.getSteps_from_door() <= distance))
                                persons.add(person);
                        }
                    });
                    while (i < persons.size()*3) {
                        i = makeBrighter();
                        try {
                            Thread.sleep(5000/255);
                        } catch (InterruptedException ee) {
                            return;
                        }
                    }
                    i = 0;
                    while (i < persons.size()*3) {
                        i = makeDarker();
                        try {
                            Thread.sleep(5000/255);
                        } catch (InterruptedException ee) {
                            return;
                        }
                    }
                });
                thread.start();
                persons.clear();
            }
        });
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread.interrupt();
            }
        });
        checkBoxN = new JCheckBox("Neutral");
        checkBoxN.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (checkBoxN.isSelected())
                    states.add(State.NEUTRAL);
                else
                    states.remove(State.NEUTRAL);
            }
        });
        checkBoxA = new JCheckBox("Angry");
        checkBoxA.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (checkBoxA.isSelected())
                    states.add(State.ANGRY);
                else
                    states.remove(State.ANGRY);
            }
        });
        checkBoxI = new JCheckBox("Interested");
        checkBoxI.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (checkBoxI.isSelected())
                    states.add(State.INTERESTED);
                else
                    states.remove(State.INTERESTED);
            }
        });
        checkBoxB = new JCheckBox("Bored");
        checkBoxB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (checkBoxB.isSelected())
                    states.add(State.BORED);
                else
                    states.remove(State.BORED);
            }
        });
        ageLabel = new JLabel("Minimum age:");
        slider = new JSlider(0,120,25);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(25);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                age = slider.getValue();
            }
        });
        distLabel = new JLabel("Maximum distance(int):");
        formattedTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    }

    private int makeBrighter(){
        int r;
        int g;
        int b;
        int i = 0;
        for (Person person: persons) {
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

    private int makeDarker(){
        int r;
        int g;
        int b;
        int final_r;
        int final_g;
        int final_b;
        int i = 0;
        for (Person person: persons) {
            final_r = person.getState().getColor().getRed();
            final_g = person.getState().getColor().getGreen();
            final_b = person.getState().getColor().getBlue();
            r = person.getColor().getRed();
            g = person.getColor().getGreen();
            b = person.getColor().getBlue();
            if (r > final_r)
                r--;
            else i++;
            if (g > final_g)
                g--;
            else i++;
            if (b > final_b)
                b--;
            else i++;
            person.setColor(new Color(r,g,b));
        }
        graphPanel.repaint();
        return i;
    }
}
