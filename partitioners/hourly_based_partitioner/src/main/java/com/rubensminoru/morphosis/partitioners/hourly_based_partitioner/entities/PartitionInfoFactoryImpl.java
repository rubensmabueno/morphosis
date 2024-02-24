package com.rubensminoru.morphosis.partitioners.hourly_based_partitioner.entities;

import com.rubensminoru.morphosis.partitioners.shared.factories.PartitionInfoFactory;


public class PartitionInfoFactoryImpl implements PartitionInfoFactory<PartitionInfo> {
    @Override
    public PartitionInfo create() {
        return new PartitionInfo();
    }
}