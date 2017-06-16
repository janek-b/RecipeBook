package com.janek.recipebook.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


@Parcel
public class Recipe {
    boolean vegetarian;
    boolean vegan;
    boolean glutenFree;
    boolean dairyFree;
    int servings;
    String sourceUrl;
    @SerializedName("extendedIngredients")
    List<Ingredient> ingredients = new ArrayList<>();
    int id;
    String title;
    @SerializedName("readyInMinutes")
    int cookTime;
    String image;
    String instructions;
    List<Instruction> fullInstructions = new ArrayList<>();

    public void setFullInstructions(List<Instruction> fullInstructions) {
        this.fullInstructions = fullInstructions;
    }

    public Recipe() {}

    public Recipe(boolean vegetarian, boolean vegan, boolean glutenFree, boolean dairyFree, int servings, String sourceUrl, List<Ingredient> ingredients, int id, String title, int cookTime, String image, String instructions, List<Instruction> fullInstructions) {
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.dairyFree = dairyFree;
        this.servings = servings;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients;
        this.id = id;
        this.title = title;
        this.cookTime = cookTime;
        this.image = image;
        this.instructions = instructions;
        this.fullInstructions = fullInstructions;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public int getServings() {
        return servings;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCookTime() {
        return cookTime;
    }

    public String getImage() {
        return image;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<Instruction> getFullInstructions() { return fullInstructions; }

}
