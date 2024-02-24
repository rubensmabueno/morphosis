package com.rubensminoru.morphosis.core;

import com.rubensminoru.morphosis.committers.shared.factories.CommitterFactory;
import com.rubensminoru.morphosis.committers.shared.config.CommitterConfig;
import com.rubensminoru.morphosis.consumers.shared.Consumer;
import com.rubensminoru.morphosis.consumers.shared.ConsumerFactory;
import com.rubensminoru.morphosis.consumers.shared.config.ConsumerConfig;
import com.rubensminoru.morphosis.core.loaders.*;
import com.rubensminoru.morphosis.partitioners.shared.Partition;
import com.rubensminoru.morphosis.partitioners.shared.Partitioner;
import com.rubensminoru.morphosis.shared.repositories.InMemoryRepository;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class ConsumerModule extends AbstractModule {
    private final ConsumerProviderLoader consumerProviderLoader;

    public ConsumerModule(ConsumerProviderLoader consumerProviderLoader) {
        this.consumerProviderLoader = consumerProviderLoader;
    }

    @Override
    protected void configure() {}

    @Provides
    @Singleton
    ConsumerConfig provideConsumerConfig() {
        return consumerProviderLoader.getConsumerConfig();
    }

    @Provides
    Consumer provideConsumer(ConsumerConfig consumerConfig) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ConsumerFactory consumerFactory = consumerProviderLoader
                .getConsumerProvider()
                .getConsumerFactoryClass()
                .getDeclaredConstructor()
                .newInstance();

        return consumerFactory.create(consumerConfig);
    }

    @Provides
    @Singleton
    InMemoryRepository<Long, Partitioner> providePartitionerRepository() {
        return new InMemoryRepository<>(
                consumerProviderLoader
                        .getPartitionerProviderLoaderList()
                        .stream()
                        .map(w -> w.getPartitionerProvider().getPartitioner())
                        .toList()
        );
    }

    @Provides
    @Singleton
    InMemoryRepository<Long, CommitterFactory> provideCommitterRepository() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<CommitterFactory> committerFactoryList = new ArrayList<>();

        for (CommitterProviderLoader committerProviderLoader : consumerProviderLoader.getCommitterProviderLoaderList()) {
            CommitterFactory consumerFactory = committerProviderLoader
                    .getCommitterProvider()
                    .getCommitterFactoryClass()
                    .getDeclaredConstructor(CommitterConfig.class)
                    .newInstance(committerProviderLoader.getCommitterConfig());

            committerFactoryList.add(consumerFactory);
        }

        return new InMemoryRepository<>(committerFactoryList);
    }

    @Provides
    @Singleton
    InMemoryRepository<Long, Partition> providePartitionRepository() {
        return new InMemoryRepository<>();
    }
}
