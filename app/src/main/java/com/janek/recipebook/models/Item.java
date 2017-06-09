package com.janek.recipebook.models;

import org.parceler.Parcel;



@Parcel
public class Item {
    int id;
    String name;
    String image;

    public Item() {}

    public Item(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
