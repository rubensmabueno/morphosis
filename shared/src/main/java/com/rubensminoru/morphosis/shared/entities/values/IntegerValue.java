package com.rubensminoru.morphosis.shared.entities.values;


public class IntegerValue extends Value {
    private final Integer value;

    public IntegerValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
