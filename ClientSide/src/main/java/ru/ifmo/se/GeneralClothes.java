package ru.ifmo.se;

import ru.ifmo.se.enums.Material;
import ru.ifmo.se.enums.Patch;

public class GeneralClothes extends Clothes {
    public GeneralClothes() {
        super();
    }

    public GeneralClothes(String colour) {
        super(colour);
    }

    public GeneralClothes(String colour, Patch[] patches) {
        super(colour, patches);
    }

    public GeneralClothes(String colour, Material material) {
        super(colour, material);
    }

    public GeneralClothes(String colour, Patch[] patches, Material material) {
        super(colour, patches, material);
    }
}