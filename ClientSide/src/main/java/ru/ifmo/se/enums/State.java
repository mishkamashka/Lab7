package ru.ifmo.se.enums;

import java.awt.*;

public enum State {
    BORED(new Color(106,90,205)),
    INTERESTED(new Color(255,165,0)),
    NEUTRAL(new Color(64,224,208)),
    ANGRY(new Color(255,0,0));

    private Color color;

    State(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }
}