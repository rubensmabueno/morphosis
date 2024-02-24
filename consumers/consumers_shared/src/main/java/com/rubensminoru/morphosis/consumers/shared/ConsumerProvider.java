package com.rubensminoru.morphosis.consumers.shared;

import com.rubensminoru.morphosis.consumers.shared.config.ConsumerConfig;

public interface ConsumerProvider {
    public Class<? extends ConsumerConfig> getConsumerConfigClass();
    public Class<? extends ConsumerFactory> getConsumerFactoryClass();
    public String getName();
}
