package com.rubensminoru.morphosis.shared.entities.fields;

import java.util.List;

public class RecordField extends Field {
    private boolean isRoot;
    private final List<Field> fields;

    public RecordField(String name, List<Field> fields) {
        super(name);

        this.fields = fields;
        this.isRoot = false;
    }

    public RecordField(List<Field> fields) {
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

    public List<Field> getFields() {
        return fields;
    }
}
