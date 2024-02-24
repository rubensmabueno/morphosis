package com.rubensminoru.morphosis.partitioners.hourly_based_partitioner.entities;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import lombok.Getter;

@Getter
public class PartitionInfo {
    private long minTimestamp;
    private long maxTimestamp;
    private long updateTimestamp;

    public PartitionInfo() {
        this.minTimestamp = Long.MAX_VALUE;
        this.maxTimestamp = Long.MIN_VALUE;
        this.updateTimestamp = Long.MIN_VALUE;
    }

    public void update(ConsumerRecord record) {
        this.updateTimestamp = System.currentTimeMillis();

        if (record.getMetadata().getTimestamp() < minTimestamp) {
            this.minTimestamp = record.getMetadata().getTimestamp();
        }

        if (record.getMetadata().getTimestamp() > maxTimestamp) {
            this.maxTimestamp = record.getMetadata().getTimestamp();
        }
    }
}