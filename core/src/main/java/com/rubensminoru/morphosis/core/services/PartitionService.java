package com.rubensminoru.morphosis.core.services;

import com.rubensminoru.morphosis.committers.shared.Committer;
import com.rubensminoru.morphosis.committers.shared.factories.CommitterFactory;
import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.partitioners.shared.Partition;
import com.rubensminoru.morphosis.shared.repositories.InMemoryRepository;

import com.google.inject.Inject;


public class PartitionService {
    private final InMemoryRepository<Long, CommitterFactory> committerFactoryRepository;

    private final InMemoryRepository<Long, Committer> committerRepository;

    @Inject
    public PartitionService(InMemoryRepository<Long, CommitterFactory> committerFactoryRepository) {
        this.committerFactoryRepository = committerFactoryRepository;
        this.committerRepository = new InMemoryRepository<>();
    }

    public void write(Partition partition, ConsumerRecord record) {
        partition.write(record);

        for (CommitterFactory committerFactory : committerFactoryRepository.all()) {
            Committer committer = committerRepository.find(partition.getId());

            if (null == committer) {
                committer = committerRepository.add(partition.getId(), committerFactory.create());
            }

            committer.write(record);
        }
    }

    public void commit(Partition partition) {
        committerRepository.find(partition.getId()).commit();
    }
}
