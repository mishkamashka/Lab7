package ru.ifmo.se;

import ru.ifmo.se.enums.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.Map;
import java.util.TreeMap;

public class GraphPanel extends JPanel implements MouseListener{
    ClientApp app;
    Map<Person, Ellipse2D> ellipsMap = new TreeMap<>();
    Graphics2D g;

    public GraphPanel(ClientApp app){
        this.app = app;

        setBackground(Color.WHITE);
    }

    public void paint(Graphics gr){
        super.paintComponent(gr);
        g = (Graphics2D) gr;
        setSize(1000,1000);
        Ellipse2D ellipse2D;
        for (Person person: app.collec){
            ellipse2D = new Ellipse2D.Double(person.getX(), person.getY(),40,20);

            g.setColor(person.getColor());
            g.draw(ellipse2D);
            ellipsMap.put(person, ellipse2D);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        new Thread(() -> {
            g.setRenderingHints(ellipsMap);
            System.out.println("whatever");
        }).start();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        JLabel label = new JLabel();
        /*for (Map.Entry<Person, Ellipse2D> entry: ellipsMap.entrySet()){
            person = entry.getKey();
            //if
        }*/
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
