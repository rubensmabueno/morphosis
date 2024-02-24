package com.rubensminoru.morphosis.consumers.shared.entities;

import com.rubensminoru.morphosis.shared.entities.GenericEntity;
import lombok.Getter;

import java.util.List;
import java.util.Map;


@Getter
public class Record extends GenericEntity {
    public Map<String, String> attributes;

    public Record(Map<String, String> attributes) {
        super();

        this.attributes = attributes;
    }

    public List<String> fields() {
        return attributes.keySet().stream().toList();
    }
}
