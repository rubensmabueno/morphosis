package com.rubensminoru.morphosis.partitioners.shared;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.shared.entities.Entity;


public interface Partitioner extends Entity<Long> {
    Boolean isEligible(Partition partition, ConsumerRecord record);

    void registerPartition(Partition partition, ConsumerRecord record);

    Boolean shouldCommit(Partition partition);
}
