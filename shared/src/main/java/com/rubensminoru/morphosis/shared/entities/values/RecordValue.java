package com.rubensminoru.morphosis.shared.entities.values;

import java.util.Set;

public class RecordValue extends Value {
    private final Set<Value> fields;

    public RecordValue(Set<Value> fields) {
        this.fields = fields;
    }

    public Set<Value> getFields() {
        return fields;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("{ ");
        for (Value value : fields) {
            string.append(value.toString()).append("; ");
        }
        string.append(" };");

        return string.toString();
    }
}
