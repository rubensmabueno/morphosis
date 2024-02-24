package com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner.repositories;

import java.util.HashMap;
import java.util.Map;

public class PartitionLastOffsetRepository {
    private final Map<Integer, Long> partitionLastOffset;

    public PartitionLastOffsetRepository() {
        this.partitionLastOffset = new HashMap<>();
    }

    public void updateOrCreate(int partition, long offset) {
        partitionLastOffset.put(partition, offset);
    }

    public Long getLastOffset(Integer key) {
        return partitionLastOffset.get(key);
    }

    public boolean lessThan(PartitionLastOffsetRepository partitionLastOffsetRepository) {
        for (Map.Entry<Integer, Long> partitionLastOffsetEntry : partitionLastOffset.entrySet()) {
            Long anotherLastOffset = partitionLastOffsetRepository.getLastOffset(partitionLastOffsetEntry.getKey());

            if (anotherLastOffset != null && anotherLastOffset <= partitionLastOffsetEntry.getValue()) {
                return false;
            }
        }

        return true;
    }
}
