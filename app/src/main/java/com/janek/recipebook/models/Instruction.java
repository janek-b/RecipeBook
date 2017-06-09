package com.janek.recipebook.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


@Parcel
public class Instruction {
    String name;
    List<Step> steps = new ArrayList<>();

    public Instruction() {}

    public Instruction(String name, List<Step> steps) {
        this.name = name;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public List<Step> getSteps() {
        return steps;
    }
}
