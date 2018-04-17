package ru.ifmo.se;

import javax.swing.*;
import java.awt.*;

public class DataPanel {
    public static void main (String ... args){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new JLabel("Hello!"), BorderLayout.WEST);
        f.setJMenuBar(new JMenuBar());
        f.pack();
        //f.setVisible(true);
    }
}
