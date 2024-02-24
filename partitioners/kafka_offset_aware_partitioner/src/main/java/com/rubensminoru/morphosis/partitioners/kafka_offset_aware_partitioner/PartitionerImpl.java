package com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner.entities.PartitionInfo;
import com.rubensminoru.morphosis.partitioners.kafka_offset_aware_partitioner.factories.PartitionInfoFactoryImpl;
import com.rubensminoru.morphosis.partitioners.shared.Partition;
import com.rubensminoru.morphosis.partitioners.shared.Partitioner;
import com.rubensminoru.morphosis.shared.entities.GenericEntity;
import com.rubensminoru.morphosis.shared.repositories.InMemoryRepository;

import java.util.List;

public class PartitionerImpl extends GenericEntity implements Partitioner {
    private final InMemoryRepository<Long, PartitionInfo> partitionInfoRepository;
    private final PartitionInfoFactoryImpl partitionInfoFactory;

    public PartitionerImpl() {
        this.partitionInfoRepository = new InMemoryRepository<>();
        this.partitionInfoFactory = new PartitionInfoFactoryImpl();
    }

    @Override
    public Boolean isEligible(Partition partition, ConsumerRecord record) {
        return true;
    }

    @Override
    public void registerPartition(Partition partition, ConsumerRecord record) {
        PartitionInfo partitionInfo = partitionInfoRepository.findOrAdd(
                partition.getId(),
                partitionInfoFactory.create()
        );

        partitionInfo.update(record);
    }

    @Override
    public Boolean shouldCommit(Partition partition) {
        PartitionInfo partitionInfo = partitionInfoRepository.find(partition.getId());

        List<PartitionInfo> partitionInfoMap = partitionInfoRepository.all();

        for(PartitionInfo internalPartitionInfo : partitionInfoMap) {
            if (internalPartitionInfo != partitionInfo) {
                if (partitionInfo.getPartitionLastOffsetRepository().lessThan(internalPartitionInfo.getPartitionLastOffsetRepository())) {
                    return false;
                }
            }
        }

        return true;
    }
}

