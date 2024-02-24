package com.rubensminoru.morphosis.partitioners.shared;

public interface PartitionerProvider {
    public Partitioner getPartitioner();
    public String getName();
}
