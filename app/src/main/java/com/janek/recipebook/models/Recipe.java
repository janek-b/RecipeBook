package com.janek.recipebook.models;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
  private String title;
  private String url;
  private String imageUrl;
  private List<String> ingredients = new ArrayList<>();

  public Recipe(String title, String url, String imageUrl, ArrayList<String> ingredients) {
    this.title = title;
    this.url = url;
    this.imageUrl = imageUrl;
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

  public List<String> getIngredients() {
    return ingredients;
  }
}
