package com.janek.recipebook.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Step {
    int number;
    String step;
    List<Item> ingredients = new ArrayList<>();
    List<Item> equipment = new ArrayList<>();


    public Step() {}

    public Step(int number, String step, List<Item> ingredients, List<Item> equipment) {
        this.number = number;
        this.step = step;
        this.ingredients = ingredients;
        this.equipment = equipment;
    }

    public int getNumber() {
        return number;
    }

    public String getStep() {
        return step;
    }

    public List<Item> getIngredients() {
        return ingredients;
    }

    public List<Item> getEquipment() {
        return equipment;
    }
}