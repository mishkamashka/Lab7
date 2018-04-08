package ru.ifmo.se;

import ru.ifmo.se.enums.Material;

public class Accessories extends Clothes {
    public Accessories() {
        super();
    }

    public Accessories(String colour) {
        super(colour);
    }

    public Accessories(String colour, Material material) {
        super(colour, material);
    }
}