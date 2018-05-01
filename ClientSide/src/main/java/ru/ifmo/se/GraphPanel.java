package ru.ifmo.se;

import ru.ifmo.se.enums.State;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Map;
import java.util.TreeMap;

public class GraphPanel extends JPanel {
    ClientApp app;
    Map<Person, Ellipse2D> ellipsMap = new TreeMap<>();

    public GraphPanel(ClientApp app){
        this.app = app;

        setBackground(Color.WHITE);
    }

    public void paint(Graphics gr){
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;
        setSize(1000,1000);
        Ellipse2D ellipse2D;
        for (Person person: app.collec){
            ellipse2D = new Ellipse2D.Double(person.getX(), person.getY(),40,20);

            g.setColor(person.getColor());
            g.draw(ellipse2D);
            ellipsMap.put(person, ellipse2D);
        }
    }
}
