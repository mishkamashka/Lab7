package ru.ifmo.se;

import ru.ifmo.se.enums.Material;
import ru.ifmo.se.enums.Patch;
import ru.ifmo.se.enums.Season;

public class Trousers extends GeneralClothes {
    Season season = Season.DEMISEASON;

    public Trousers() {
        super();
    }

    public Trousers(String colour) {
        super(colour);
    }

    public Trousers(String colour, Season season) {
        super(colour);
        this.season = season;
    }

    public Trousers(String colour, Season season, Patch[] patches) {
        super(colour, patches);
        this.season = season;
    }

    public Trousers(String colour, Material material) {
        super(colour, material);
    }

    public Trousers(String colour, Patch[] patches, Material material) {
        super(colour, patches, material);
    }

    @Override
    public String toString() {
        if (this.getMaterial() != Material.NONE)
            return super.getColour() + season + " Trousers made from " + this.getMaterial();
        else
            return super.getColour() + " " + season + " Trousers";
    }
}
