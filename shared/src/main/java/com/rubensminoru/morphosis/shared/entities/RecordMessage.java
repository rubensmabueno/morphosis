package com.rubensminoru.morphosis.shared.entities;

import com.rubensminoru.morphosis.shared.entities.values.Value;
import lombok.Getter;


@Getter
public class RecordMessage extends GenericEntity {
    public Schema schema;
    private final Value value;

    public RecordMessage(Schema schema, Value value) {
        this.schema = schema;
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }
}
