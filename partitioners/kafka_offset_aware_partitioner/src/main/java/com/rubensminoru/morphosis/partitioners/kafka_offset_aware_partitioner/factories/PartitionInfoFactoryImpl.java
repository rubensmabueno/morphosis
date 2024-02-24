package com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner.factories;

import com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner.entities.PartitionInfo;
import com.rubensminoru.morphosis.partitioners.shared.factories.PartitionInfoFactory;


public class PartitionInfoFactoryImpl implements PartitionInfoFactory<PartitionInfo> {
    @Override
    public PartitionInfo create() {
        return new PartitionInfo();
    }
}
