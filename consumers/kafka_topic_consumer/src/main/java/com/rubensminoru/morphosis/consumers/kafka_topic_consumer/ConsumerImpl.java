package com.rubensminoru.morphosis.consumers.kafka_topic_consumer;

import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.config.ConsumerConfigImpl;
import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.entities.RecordMetadataImpl;
import com.rubensminoru.morphosis.consumers.shared.Consumer;
import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.shared.repositories.InMemoryRepository;

import java.util.List;

public class ConsumerImpl implements Consumer {
    private final ConsumerConfigImpl consumerConfig;
    private final TopicConsumer topicConsumer;
    private InMemoryRepository<String, RecordMetadataImpl> recordMetadataRepository;

    public ConsumerImpl(ConsumerConfigImpl consumerConfig) {
        this.consumerConfig = consumerConfig;
        this.recordMetadataRepository = new InMemoryRepository<>();
        this.topicConsumer = new TopicConsumer(
                consumerConfig.getBoostrapServer(),
                consumerConfig.getGroupId(),
                consumerConfig.getPoolDuration(),
                consumerConfig.getKeyFormat(),
                consumerConfig.getValueFormat(),
                this.recordMetadataRepository
        );
    }

    @Override
    public void initialize() {
        this.topicConsumer.subscribe(this.consumerConfig.getTopicName());
    }

    @Override
    public List<ConsumerRecord> consume() {
        return this.topicConsumer.consume();
    }
}
