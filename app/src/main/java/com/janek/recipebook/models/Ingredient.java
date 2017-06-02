package com.janek.recipebook.models;

import org.parceler.Parcel;

@Parcel
public class Ingredient {
  private int id;
  private String aisle;
  private String image;
  private String name;
  private float amount;
  private String unit;
  private String unitShort;
  private String unitLong;
  private String originalString;
  private String[] metaInformation;

  public Ingredient() {}

  public Ingredient(int id, String aisle, String image, String name, float amount, String unit, String unitShort, String unitLong, String originalString, String[] metaInformation) {
    this.id = id;
    this.aisle = aisle;
    this.image = image;
    this.name = name;
    this.amount = amount;
    this.unit = unit;
    this.unitShort = unitShort;
    this.unitLong = unitLong;
    this.originalString = originalString;
    this.metaInformation = metaInformation;
  }

  public int getId() {
    return id;
  }

  public String getAisle() {
    return aisle;
  }

  public String getImage() {
    return image;
  }

  public String getName() {
    return name;
  }

  public float getAmount() {
    return amount;
  }

  public String getUnit() {
    return unit;
  }

  public String getUnitShort() {
    return unitShort;
  }

  public String getUnitLong() {
    return unitLong;
  }

  public String getOriginalString() {
    return originalString;
  }

  public String[] getMetaInformation() {
    return metaInformation;
  }
}
