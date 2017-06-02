package com.janek.recipebook.models;

public class RecipeList {
  int id;
  String title;
  int readyInMinutes;
  String image;
  String[] imageUrls;

  public RecipeList(int id, String title, int readyInMinutes, String image, String[] imageUrls) {
    this.id = id;
    this.title = title;
    this.readyInMinutes = readyInMinutes;
    this.image = image;
    this.imageUrls = imageUrls;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public int getReadyInMinutes() {
    return readyInMinutes;
  }

  public String getImage() {
    return image;
  }

  public String[] getImageUrls() {
    return imageUrls;
  }
}
