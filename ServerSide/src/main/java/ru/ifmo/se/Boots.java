package ru.ifmo.se;

import ru.ifmo.se.enums.Material;
import ru.ifmo.se.enums.Season;

public class Boots extends Shoes {
    Season season = Season.DEMISEASON;

    public Boots() {
        super();
    }

    public Boots(String colour) {
        super(colour);
    }

    public Boots(String colour, Season season) {
        super(colour);
        this.season = season;
    }

    public Boots(String colour, Material material) {
        super(colour, material);
    }

    @Override
    public String toString() {
        if (this.getMaterial() != Material.NONE)
            return super.getColour() + " " + season + " Boots made from " + this.getMaterial();
        else
            return super.getColour() + " " + season + " Boots";
    }
}