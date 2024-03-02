package com.rubensminoru.morphosis.partitioners.shared;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.consumers.shared.entities.RecordMetadata;

import com.rubensminoru.morphosis.shared.entities.GenericEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Partition extends GenericEntity {
    private final List<RecordMetadata> recordMetadataList;

    public Partition() {
        super();

        this.recordMetadataList = new ArrayList<>();
    }

    public void write(ConsumerRecord record) {
        recordMetadataList.add(record.getMetadata());
    }
}
