package com.peopledemo1;

public class SimilarPeople {
    private String name;
    private Double probability;

    public SimilarPeople(String name, Double probability) {
        this.name = name;
        this.probability = probability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }
}
