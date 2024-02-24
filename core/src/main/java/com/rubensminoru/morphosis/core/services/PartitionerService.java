package com.rubensminoru.morphosis.core.services;

import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.partitioners.shared.Partition;
import com.rubensminoru.morphosis.partitioners.shared.Partitioner;
import com.rubensminoru.morphosis.shared.repositories.InMemoryRepository;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;


public class PartitionerService {
    private final PartitionService partitionService;

    private final InMemoryRepository<Long, Partitioner> partitionerRepository;

    private final InMemoryRepository<Long, Partition> partitionRepository;

    @Inject
    public PartitionerService(
            InMemoryRepository<Long, Partitioner> partitionerRepository,
            InMemoryRepository<Long, Partition> partitionRepository,
            PartitionService partitionService
    ) {
        this.partitionerRepository = partitionerRepository;
        this.partitionRepository = partitionRepository;
        this.partitionService = partitionService;
    }

    public Partition partition(ConsumerRecord record) {
        Boolean eligible;

        for (Partition partition : this.partitionRepository.all()) {
            eligible = false;

            for (Partitioner partitioner : partitionerRepository.all()) {
                eligible = eligible || partitioner.isEligible(partition, record);
            }

            if (eligible) {
                return partition;
            }
        }

        return this.partitionRepository.add(new Partition());
    }

    public void write(Partition partition, ConsumerRecord record) {
        partitionService.write(partition, record);

        for (Partitioner partitioner : partitionerRepository.all()) {
            partitioner.registerPartition(partition, record);
        }
    }

    public List<Partition> commit() {
        List<Partition> partitionList = new ArrayList<>();

        for (Partition partition : this.partitionRepository.all()) {
            Boolean commitable = true;

            for (Partitioner partitioner : partitionerRepository.all()) {
                commitable = commitable && partitioner.shouldCommit(partition);
            }

            if (commitable) {
                partitionList.add(partition);
                partitionService.commit(partition);
            }
        }

        return partitionList;
    }
}
