package ru.ifmo.se;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Set;
import java.util.TreeSet;

public class GraphPanel extends JPanel {
    JButton button;
    Set<Ellipse2D> ellipsSet = new TreeSet<>();

    public GraphPanel(ClientApp app){
        //Graphics2D graphics = new G
        Ellipse2D ellipse2D = new Ellipse2D.Float(1,2,3,4);
        //app.collec.forEach(person -> );
    }
}
