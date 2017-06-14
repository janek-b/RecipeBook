package com.janek.recipebook.models;

import org.parceler.Parcel;

@Parcel
public class RecipeSearchSuggestion {
    int id;
    String title;

    public RecipeSearchSuggestion() {}

    public RecipeSearchSuggestion(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
