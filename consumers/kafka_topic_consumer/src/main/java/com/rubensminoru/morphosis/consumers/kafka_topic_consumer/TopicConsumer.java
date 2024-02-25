package com.rubensminoru.morphosis.consumers.kafka_topic_consumer;

import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.deserializers.JsonDeserializer;
import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.deserializers.StringDeserializer;
import com.rubensminoru.morphosis.consumers.kafka_topic_consumer.entities.RecordMetadataImpl;
import com.rubensminoru.morphosis.consumers.shared.entities.ConsumerRecord;
import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import com.rubensminoru.morphosis.shared.entities.Schema;
import com.rubensminoru.morphosis.shared.entities.fields.Field;
import com.rubensminoru.morphosis.shared.entities.fields.RecordField;
import com.rubensminoru.morphosis.shared.entities.values.RecordValue;
import com.rubensminoru.morphosis.shared.entities.values.Value;
import com.rubensminoru.morphosis.shared.repositories.InMemoryRepository;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

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
            String keyFormat,
            String valueFormat,
            InMemoryRepository<String, RecordMetadataImpl> recordMetadataRepository
    ) {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, formatToDeserializer(keyFormat));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, formatToDeserializer(valueFormat));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);

        this.consumer = new KafkaConsumer<>(props);
        this.poolDuration = poolDuration;
        this.recordMetadataRepository = recordMetadataRepository;
    }

    public Class<? extends Deserializer> formatToDeserializer(String format) {
        if (format.equals("json")) {
            return JsonDeserializer.class;
        } else {
            return StringDeserializer.class;
        }
    }

    public void subscribe(String topicName) {
        this.consumer.subscribe(List.of(topicName));
    }

    public List<ConsumerRecord> consume() {
        ConsumerRecords<RecordMessage, RecordMessage> poll = this.consumer.poll(Duration.of(this.poolDuration, ChronoUnit.MILLIS));

        List<ConsumerRecord> list = new ArrayList<>();

        Iterator<org.apache.kafka.clients.consumer.ConsumerRecord<RecordMessage, RecordMessage>> iterator = poll.iterator();

        while (iterator.hasNext()) {
            org.apache.kafka.clients.consumer.ConsumerRecord<RecordMessage, RecordMessage> consumerRecord = iterator.next();

            RecordMessage keyMessage = consumerRecord.key();
            Field keyField = keyMessage.getSchema().getField();
            keyField.setName("key");

            RecordMessage valueMessage = consumerRecord.value();
            Field valueField = valueMessage.getSchema().getField();
            valueField.setName("value");

            LinkedHashMap<Field, Value> recordValue = new LinkedHashMap<>();
            recordValue.put(keyField, keyMessage.getValue());
            recordValue.put(valueField, valueMessage.getValue());

            list.add(
                new ConsumerRecord(
                    new RecordMessage(
                            new Schema(
                                    new RecordField(
                                            List.of(keyField, valueField)
                                    )
                            ),
                            new RecordValue(
                                    recordValue
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
                )
            );
        }

        return list;
    }
}
