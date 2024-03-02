package com.rubensminoru.morphosis.partitioners.shared;

public interface PartitionerProvider {
    Partitioner getPartitioner();
    String getName();
}
