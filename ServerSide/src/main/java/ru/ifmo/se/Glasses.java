package ru.ifmo.se;

public class Glasses extends Accessories {
    public Glasses() {
        super();
    }

    public Glasses(String colour) {
        super(colour);
    }

    @Override
    public String toString() {
        return super.getColour() + " Glasses";
    }
}
