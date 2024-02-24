package com.rubensminoru.morphosis.consumers.shared.entities;

import com.rubensminoru.morphosis.shared.entities.GenericEntity;

import lombok.Getter;

@Getter
public class ConsumerRecord<K extends RecordMetadata> extends GenericEntity {
    private Record record;
    private K metadata;

    public ConsumerRecord(Record record, K metadata) {
        super();

        this.record = record;
        this.metadata = metadata;
    }

    public String toString() {
        return "Metadata: " + metadata + " / Record: " + record;
    }
}
