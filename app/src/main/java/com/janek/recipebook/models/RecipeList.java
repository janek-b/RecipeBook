package com.janek.recipebook.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RecipeList {
  int id;
  String title;
  @SerializedName("readyInMinutes")
  int cookTime;
  String image;
  String[] imageUrls;

  public RecipeList() {}

  public RecipeList(int id, String title, int cookTime, String image, String[] imageUrls) {
    this.id = id;
    this.title = title;
    this.cookTime = cookTime;
    this.image = image;
    this.imageUrls = imageUrls;
  }

  public int getId() { return id; }
  public String getTitle() {
    return title;
  }
  public int getCookTime() {
    return cookTime;
  }
  public String getImage() {
    return image;
  }
  public String[] getImageUrls() {
    return imageUrls;
  }
}
