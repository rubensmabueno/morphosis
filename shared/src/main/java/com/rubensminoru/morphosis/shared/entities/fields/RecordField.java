package com.rubensminoru.morphosis.shared.entities.fields;

import java.util.Set;

public class RecordField extends Field {
    private boolean isRoot;
    private final Set<Field> fields;

    public RecordField(String name, Set<Field> fields) {
        super(name);

        this.fields = fields;
        this.isRoot = false;
    }

    public RecordField(Set<Field> fields) {
        super("root");

        this.fields = fields;
        this.isRoot = true;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        this.fields.add(field);
    }
}
