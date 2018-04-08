package ru.ifmo.se;

import ru.ifmo.se.enums.Material;
import ru.ifmo.se.enums.Patch;

import java.io.Serializable;

public abstract class Clothes implements Serializable {
    private String colour;
    private Patch[] patches = {Patch.NONE, Patch.NONE, Patch.NONE, Patch.NONE, Patch.NONE};
    private Material material = Material.NONE;

    public Clothes() {
        this.colour = "no colour";
    }

    public Clothes(String colour) {
        this.colour = colour;
    }

    public Clothes(String colour, Patch[] patches) {
        this.colour = colour;
        this.patches = patches;
    }

    public Clothes(String colour, Material material) {
        this.colour = colour;
        this.material = material;
    }

    public Clothes(String colour, Patch[] patches, Material material) {
        this.colour = colour;
        this.patches = patches;
        this.material = material;
    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        if (colour == null)
            return;
        this.colour = colour;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        if (material == null)
            return;
        this.material = material;
    }

    public void addPatch(Patch patch) {
        if (patch == null)
            return;
        int i = 0;
        while (i <= 4 && this.patches[i] != Patch.NONE) {
            i++;
        }
        if (i == 4 && this.patches[i] != Patch.NONE)
            System.out.println("No more patches can be added.");
        if (this.patches[i] == Patch.NONE)
            this.patches[i] = patch;

    }

    @Override
    public String toString() {
        return this.colour + " " + " " + this.getClass();
    }
}