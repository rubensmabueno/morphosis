package com.rubensminoru.morphosis.consumers.shared;

import com.rubensminoru.morphosis.consumers.shared.config.ConsumerConfig;

public interface ConsumerProvider {
    Class<? extends ConsumerConfig> getConsumerConfigClass();
    Class<? extends ConsumerFactory> getConsumerFactoryClass();
    String getName();
}
