package com.rubensminoru.morphosis.consumers.shared.entities;

import com.rubensminoru.morphosis.shared.entities.GenericEntity;

import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import lombok.Getter;

@Getter
public class ConsumerRecord<K extends RecordMetadata> extends GenericEntity {
    private final RecordMessage message;
    private final K metadata;

    public ConsumerRecord(RecordMessage message, K metadata) {
        super();

        this.message = message;
        this.metadata = metadata;
    }

    public String toString() {
        return "Metadata: " + metadata + " / Record: " + message;
    }
}
