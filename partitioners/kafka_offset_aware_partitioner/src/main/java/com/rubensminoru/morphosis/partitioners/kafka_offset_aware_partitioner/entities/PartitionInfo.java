package com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner.entities;

import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.entities.RecordMetadataImpl;
import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner.repositories.PartitionLastOffsetRepository;
import com.rubensminoru.morphosis.shared.entities.GenericEntity;

import lombok.Getter;

@Getter
public class PartitionInfo extends GenericEntity {
    private final PartitionLastOffsetRepository partitionLastOffsetRepository;

    public PartitionInfo() {
        super();

        this.partitionLastOffsetRepository = new PartitionLastOffsetRepository();
    }

    public void update(ConsumerRecord record) {
        RecordMetadataImpl metadata = (RecordMetadataImpl) record.getMetadata();

        partitionLastOffsetRepository.updateOrCreate(metadata.getPartition(), metadata.getOffset());
    }
}