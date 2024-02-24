package com.rubensminoru.morphosis.consumers.kafka_topic_consumer;

import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.entities.RecordMetadataImpl;
import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.consumers.shared.entities.Record;
import com.rubensminoru.morphosis.shared.repositories.InMemoryRepository;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TopicConsumer {
    private final int poolDuration;
    private final KafkaConsumer consumer;
    private final InMemoryRepository<String, RecordMetadataImpl> recordMetadataRepository;

    public TopicConsumer(
            String bootstrapServers,
            String groupId,
            int poolDuration,
            InMemoryRepository<String, RecordMetadataImpl> recordMetadataRepository
    ) {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);

        this.consumer = new KafkaConsumer<>(props);
        this.poolDuration = poolDuration;
        this.recordMetadataRepository = recordMetadataRepository;
    }

    public void subscribe(String topicName) {
        this.consumer.subscribe(List.of(topicName));
    }

    public List<ConsumerRecord> consume() {
        ConsumerRecords<String, String> poll = this.consumer.poll(Duration.of(this.poolDuration, ChronoUnit.MILLIS));

        List<ConsumerRecord> list = new ArrayList<>();

        Iterator<org.apache.kafka.clients.consumer.ConsumerRecord<String, String>> iterator = poll.iterator();
        while (iterator.hasNext()) {
            org.apache.kafka.clients.consumer.ConsumerRecord<String, String> consumerRecord = iterator.next();
            list.add(new ConsumerRecord(
                    new Record(
                            Map.of(
                                "key", consumerRecord.key(),
                                "value", consumerRecord.value()
                            )
                    ),
                    recordMetadataRepository.add(
                            new RecordMetadataImpl(
                                    consumerRecord.topic(),
                                    consumerRecord.partition(),
                                    consumerRecord.offset(),
                                    consumerRecord.timestamp()
                            )
                    )
            ));
        }

        return list;
    }
}
