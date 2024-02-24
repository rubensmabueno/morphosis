package com.rubensminoru.morphosis.committers.s3_committer.entities;

import lombok.Getter;

import java.util.Map;


@Getter
public class Schema {
    private Map<String, String> fields;

    public Schema() {
        this.fields = Map.of(
                "key", "string",
                "value", "string"
        );
    }

    public String getType(String key) {
        return fields.get(key);
    }
}
