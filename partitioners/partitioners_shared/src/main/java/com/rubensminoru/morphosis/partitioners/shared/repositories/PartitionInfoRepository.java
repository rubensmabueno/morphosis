package com.rubensminoru.morphosis.partitioners.shared.repositories;

import com.rubensminoru.morphosis.partitioners.shared.Partition;
import com.rubensminoru.morphosis.partitioners.shared.factories.PartitionInfoFactory;

import java.util.HashMap;
import java.util.Map;


public class PartitionInfoRepository<T> {
    private final PartitionInfoFactory<T> partitionInfoFactory;
    private final Map<Partition, T> partitionInfoMap;

    public PartitionInfoRepository(PartitionInfoFactory<T> partitionInfoFactory) {
        this.partitionInfoMap = new HashMap<>();
        this.partitionInfoFactory = partitionInfoFactory;
    }

    public T get(Partition partition) {
        return partitionInfoMap.get(partition);
    }

    public T getOrCreate(Partition partition) {
        if (!partitionInfoMap.containsKey(partition)) {
            partitionInfoMap.put(partition, partitionInfoFactory.create());
        }

        return get(partition);
    }

    public Map<Partition,T> all() {
        return partitionInfoMap;
    }
}