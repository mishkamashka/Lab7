package ru.ifmo.se;

import ru.ifmo.se.enums.Material;

public class Hat extends Accessories {

    public Hat() {
        super();
    }

    public Hat(String colour) {
        super(colour);
    }

    public Hat(String colour, Material material) {
        super(colour, material);
    }

    @Override
    public String toString() {
        if (this.getMaterial() != Material.NONE)
            return super.getColour() + " Hat made from " + this.getMaterial();
        else
            return super.getColour() + " Hat";
    }
}