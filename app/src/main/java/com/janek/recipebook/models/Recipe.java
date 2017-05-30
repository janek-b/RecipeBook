package com.janek.recipebook.models;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
  private String title;
  private String url;
  private String imageUrl;
  private double recipeId;
  private double rating;
  private List<String> ingredients = new ArrayList<>();

  public Recipe(String title, String url, String imageUrl, double recipeId, double rating, ArrayList<String> ingredients) {
    this.title = title;
    this.url = url;
    this.imageUrl = imageUrl;
    this.recipeId = recipeId;
    this.rating = rating;
    this.ingredients = ingredients;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public double getRecipeId() {
    return recipeId;
  }

  public double getRating() {
    return rating;
  }

  public List<String> getIngredients() {
    return ingredients;
  }
}
