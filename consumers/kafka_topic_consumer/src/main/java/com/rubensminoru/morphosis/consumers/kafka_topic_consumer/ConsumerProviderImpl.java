package com.rubensminoru.morphosis.consumers.kafka_topic_consumer;

import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.config.ConsumerConfigImpl;
import com.rubensminoru.morphosis.consumers.shared.ConsumerFactory;
import com.rubensminoru.morphosis.consumers.shared.ConsumerProvider;
import com.rubensminoru.morphosis.consumers.shared.config.ConsumerConfig;

public class ConsumerProviderImpl implements ConsumerProvider {
    @Override
    public Class<? extends ConsumerConfig> getConsumerConfigClass() {
        return ConsumerConfigImpl.class;
    }

    @Override
    public Class<? extends ConsumerFactory> getConsumerFactoryClass() {
        return ConsumerFactoryImpl.class;
    }

    @Override
    public String getName() {
        return "kafka_topic";
    }
}
