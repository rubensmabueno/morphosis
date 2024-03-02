package com.rubensminoru.morphosis.shared.entities.fields;

public abstract class Field {
    private String name;

    public Field(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
