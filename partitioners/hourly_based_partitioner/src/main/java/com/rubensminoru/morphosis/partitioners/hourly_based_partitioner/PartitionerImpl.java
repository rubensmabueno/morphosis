package com.rubensminoru.morphosis.partitioners.hourly_based_partitioner;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.partitioners.hourly_based_partitioner.entities.PartitionInfo;
import com.rubensminoru.morphosis.partitioners.hourly_based_partitioner.entities.PartitionInfoFactoryImpl;
import com.rubensminoru.morphosis.partitioners.shared.repositories.PartitionInfoRepository;
import com.rubensminoru.morphosis.partitioners.shared.Partition;
import com.rubensminoru.morphosis.partitioners.shared.Partitioner;
import com.rubensminoru.morphosis.shared.entities.GenericEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class PartitionerImpl extends GenericEntity implements Partitioner {
    private final long waitFor;

    private final PartitionInfoRepository<PartitionInfo> partitionInfoRepository;

    public PartitionerImpl() {
        this.partitionInfoRepository = new PartitionInfoRepository<>(new PartitionInfoFactoryImpl());
        this.waitFor = 6000;
    }

    @Override
    public Boolean isEligible(Partition partition, ConsumerRecord record) {
        PartitionInfo partitionInfo = partitionInfoRepository.get(partition);

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(partitionInfo.getMinTimestamp()), ZoneOffset.UTC);
        LocalDateTime roundedDateTime = dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        long nextHourTimestamp = roundedDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        return partitionInfo.getMinTimestamp() <= record.getMetadata().getTimestamp() &&
                record.getMetadata().getTimestamp() < nextHourTimestamp;
    }

    @Override
    public void registerPartition(Partition partition, ConsumerRecord record) {
        PartitionInfo partitionInfo = partitionInfoRepository.getOrCreate(partition);

        partitionInfo.update(record);
    }

    @Override
    public Boolean shouldCommit(Partition partition) {
        PartitionInfo partitionInfo = partitionInfoRepository.get(partition);

        System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli(partitionInfo.getUpdateTimestamp()), ZoneOffset.UTC));
        System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneOffset.UTC));
        System.out.println((partitionInfo.getUpdateTimestamp() + this.waitFor < System.currentTimeMillis()));

        return (partitionInfo.getUpdateTimestamp() + this.waitFor < System.currentTimeMillis());
    }
}

