package ru.ifmo.se;

import ru.ifmo.se.enums.Material;
import ru.ifmo.se.enums.Patch;

public class Shirt extends GeneralClothes {
    public Shirt() {
        super();
    }

    public Shirt(String colour) {
        super(colour);
    }

    public Shirt(String colour, Patch[] patches) {
        super(colour, patches);
    }

    public Shirt(String colour, Material material) {
        super(colour, material);
    }

    public Shirt(String colour, Patch[] patches, Material material) {
        super(colour, patches, material);
    }

    @Override
    public String toString() {
        if (this.getMaterial() != Material.NONE)
            return super.getColour() + " Shirt made from " + this.getMaterial();
        else
            return super.getColour() + " Shirt";
    }
}