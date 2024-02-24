package com.rubensminoru.morphosis.consumers.kafka_topic_consumer;

import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.config.ConsumerConfigImpl;
import com.rubensminoru.morphosis.consumers.shared.Consumer;
import com.rubensminoru.morphosis.consumers.shared.ConsumerFactory;
import com.rubensminoru.morphosis.consumers.shared.config.ConsumerConfig;

public class ConsumerFactoryImpl implements ConsumerFactory {
    @Override
    public Consumer create(ConsumerConfig config) {
        return new ConsumerImpl((ConsumerConfigImpl) config);
    }
}
