package com.rubensminoru.morphosis.core.services;

import com.rubensminoru.morphosis.consumers.shared.Consumer;
import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.partitioners.shared.Partition;

import com.google.inject.Inject;

import java.util.List;


public class ConsumerService {
    private final Consumer consumer;
    private final PartitionerService partitionerService;

    @Inject
    public ConsumerService(Consumer consumer, PartitionerService partitionerService) {
        this.consumer = consumer;
        this.partitionerService = partitionerService;
    }

    public void initialize() {
        this.consumer.initialize();
    }

    public void consume() {
        while (true) {
            List<ConsumerRecord> records = this.consumer.consume();

            for (ConsumerRecord record : records) {
                Partition partition = partitionerService.partition(record);
                partitionerService.write(partition, record);
            }

            List<Partition> commitablePartitions = partitionerService.commit();
        }
    }
}
