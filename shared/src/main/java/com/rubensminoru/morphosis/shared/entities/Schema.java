package com.rubensminoru.morphosis.shared.entities;

import com.rubensminoru.morphosis.shared.entities.fields.Field;


public class Schema extends GenericEntity {
    private final Field field;

    public Schema(Field field) {
        super();

        this.field = field;
    }

    public Field getField() {
        return field;
    }
}
