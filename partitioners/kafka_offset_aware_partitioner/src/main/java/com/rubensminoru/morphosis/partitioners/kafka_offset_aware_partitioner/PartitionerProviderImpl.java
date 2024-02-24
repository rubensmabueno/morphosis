package com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner;

import com.rubensminoru.morphosis.partitioners.shared.Partitioner;
import com.rubensminoru.morphosis.partitioners.shared.PartitionerProvider;

public class PartitionerProviderImpl implements PartitionerProvider {
    @Override
    public Partitioner getPartitioner() {
        return new PartitionerImpl();
    }

    @Override
    public String getName() {
        return "kafka_offset_aware";
    }
}

