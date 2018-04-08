package ru.ifmo.se;

import ru.ifmo.se.enums.Material;

public class Shoes extends Clothes {

    public Shoes() {
        super();
    }

    public Shoes(String colour) {
        super(colour);
    }

    public Shoes(String colour, Material material) {
        super(colour, material);
    }
}