package com.rubensminoru.morphosis.core;

import com.rubensminoru.morphosis.committers.shared.CommitterProvider;
import com.rubensminoru.morphosis.consumers.shared.ConsumerProvider;
import com.rubensminoru.morphosis.core.loaders.ConfigLoader;
import com.rubensminoru.morphosis.core.loaders.ProviderCatalogLoader;
import com.rubensminoru.morphosis.core.loaders.ConsumerProviderLoader;
import com.rubensminoru.morphosis.partitioners.shared.PartitionerProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class MainModule extends AbstractModule {
    @Override
    protected void configure() {}

    @Provides
    @Singleton
    ProviderCatalogLoader<ConsumerProvider> provideConsumerProviderCatalogLoader() {
        return ProviderCatalogLoader.load(ConsumerProvider.class);
    }

    @Provides
    @Singleton
    ProviderCatalogLoader<PartitionerProvider> providePartitionerProviderCatalogLoader() {
        return ProviderCatalogLoader.load(PartitionerProvider.class);
    }

    @Provides
    @Singleton
    ProviderCatalogLoader<CommitterProvider> provideCommitterProviderCatalogLoader() {
        return ProviderCatalogLoader.load(CommitterProvider.class);
    }

    @Provides
    @Singleton
    ConfigLoader provideConfigLoader(
            ProviderCatalogLoader<ConsumerProvider> consumerProviderCatalogLoader,
            ProviderCatalogLoader<PartitionerProvider> partitionerProviderCatalogLoader,
            ProviderCatalogLoader<CommitterProvider> committerProviderCatalogLoader
    ) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return ConfigLoader.load(
                "application.yaml",
                consumerProviderCatalogLoader.getProviderCatalog(),
                partitionerProviderCatalogLoader.getProviderCatalog(),
                committerProviderCatalogLoader.getProviderCatalog()
        );
    }

    @Provides
    @Singleton
    List<ConsumerProviderLoader> provideConsumerProvider(ConfigLoader configLoader) {
        return configLoader.getConsumerProviderLoader();
    }
}
