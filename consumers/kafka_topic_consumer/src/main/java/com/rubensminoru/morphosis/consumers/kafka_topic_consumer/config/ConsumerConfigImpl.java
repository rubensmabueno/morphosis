package com.rubensminoru.morphosis.consumers.kafka_topic_consumer.config;

import com.rubensminoru.morphosis.consumers.shared.config.ConsumerConfig;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ConsumerConfigImpl implements ConsumerConfig {
    private String type;
    private String boostrapServer;
    private String groupId;
    private int poolDuration;
    private String topicName;
    private int partitionForget;
    private String keyFormat;
    private String valueFormat;
}
