package com.rubensminoru.morphosis.consumers.kafka_topic_consumer.entities;

import com.rubensminoru.morphosis.consumers.shared.entities.RecordMetadata;
import com.rubensminoru.morphosis.shared.entities.Entity;
import lombok.Getter;


@Getter
public class RecordMetadataImpl implements RecordMetadata, Entity<String> {
    private final String topic;
    private final int partition;
    private final long offset;
    private final long timestamp;


    public RecordMetadataImpl(String topic, int partition, long offset, long timestamp) {
        super();

        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.timestamp = timestamp;
    }

    public String getId() {
        return this.topic + this.partition + this.offset;
    }
}
