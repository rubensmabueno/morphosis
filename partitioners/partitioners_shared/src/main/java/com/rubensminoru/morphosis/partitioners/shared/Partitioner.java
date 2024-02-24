package com.rubensminoru.morphosis.partitioners.shared;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.shared.entities.Entity;


public interface Partitioner extends Entity<Long> {
    public Boolean isEligible(Partition partition, ConsumerRecord record);

    public void registerPartition(Partition partition, ConsumerRecord record);

    public Boolean shouldCommit(Partition partition);
}
