package ru.ifmo.se;

import ru.ifmo.se.enums.Material;
import ru.ifmo.se.enums.Patch;
import ru.ifmo.se.enums.Season;

public class Jacket extends GeneralClothes {
    boolean buttonedUp;
    Season season = Season.DEMISEASON;

    public Jacket() {
        super();
    }

    public Jacket(String colour) {
        super(colour);
    }

    public Jacket(String colour, Season season) {
        super(colour);
        this.season = season;
    }

    public Jacket(String colour, Season season, Patch[] patches) {
        super(colour, patches);
        this.season = season;
    }

    public Jacket(String colour, Material material) {
        super(colour, material);
    }

    public Jacket(String colour, Patch[] patches, Material material) {
        super(colour, patches, material);
    }

    void doUp() {
        this.buttonedUp = true;
    }

    void unDo() {
        this.buttonedUp = false;
    }

    public String buttonedToString() {
        if (this.buttonedUp)
            return ("buttoned up");
        else
            return ("unbuttoned");
    }

    @Override
    public String toString() {
        if (this.getMaterial() != Material.NONE)
            return super.getColour() + season + " " + this.buttonedToString() + " Jacket made from " + this.getMaterial();
        else
            return super.getColour() + " " + season + " " + this.buttonedToString() + " Jacket";
    }

}