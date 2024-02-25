package com.rubensminoru.morphosis.shared.entities.values;

import com.rubensminoru.morphosis.shared.entities.fields.Field;

import java.util.LinkedHashMap;
import java.util.Map;

public class RecordValue extends Value {
    private final LinkedHashMap<Field, Value> fields;

    public RecordValue(LinkedHashMap<Field, Value> fields) {
        this.fields = fields;
    }

    public Map<Field, Value> getFields() {
        return fields;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("{ ");
        for (Map.Entry<Field, Value> fieldValueEntry : fields.entrySet()) {
            string.append(fieldValueEntry.getKey().getName()).append(": ");
            string.append(fieldValueEntry.getValue().toString()).append("; ");
        }
        string.append(" };");

        return string.toString();
    }
}
