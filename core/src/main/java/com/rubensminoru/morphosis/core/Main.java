package com.rubensminoru.morphosis.core;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.rubensminoru.morphosis.core.loaders.ConfigLoader;
import com.rubensminoru.morphosis.core.loaders.ConsumerProviderLoader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rubensminoru.morphosis.core.services.ConsumerService;


public class Main {
    static class ConsumerProcessor implements Runnable {
        private final ConsumerProviderLoader consumerProviderLoader;

        ConsumerProcessor(ConsumerProviderLoader consumerProviderLoader) {
            this.consumerProviderLoader = consumerProviderLoader;
        }

        @Override
        public void run() {
            Injector consumerInjector = Guice.createInjector(new ConsumerModule(consumerProviderLoader));
            ConsumerService consumerService = consumerInjector.getInstance(ConsumerService.class);

            consumerService.initialize();
            consumerService.consume();
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());

        ConfigLoader configLoader = injector.getInstance(ConfigLoader.class);

        threadedProcess(configLoader.getConsumerProviderLoader());
    }

    static void threadedProcess(List<ConsumerProviderLoader> consumerProviderLoaderList) {
        ExecutorService executorService = Executors.newFixedThreadPool(consumerProviderLoaderList.size());

        for (ConsumerProviderLoader consumerProviderLoader : consumerProviderLoaderList) {
            executorService.execute(new ConsumerProcessor(consumerProviderLoader));
        }
        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
