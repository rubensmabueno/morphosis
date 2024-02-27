package com.rubensminoru.morphosis.consumers.kafka_topic_consumer.deserializers;

import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import com.rubensminoru.morphosis.shared.entities.Schema;
import com.rubensminoru.morphosis.shared.services.JacksonToRecordSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class JsonDeserializer implements Deserializer<RecordMessage> {
    private final ObjectMapper objectMapper;

    private final Map<String, Schema> topicSchemaMap;

    public JsonDeserializer() {
        this.objectMapper = new ObjectMapper();
        this.topicSchemaMap = new HashMap<>();
    }

    public RecordMessage deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            RecordMessage recordMessage = JacksonToRecordSerializer.parse(objectMapper.readTree(data), this.topicSchemaMap.get(topic));
            this.topicSchemaMap.put(topic, recordMessage.getSchema());

            return recordMessage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RecordMessage deserialize(String topic, Headers headers, ByteBuffer data) {
        if (data == null) {
            return null;
        }
        byte[] bytes = new byte[data.remaining()];
        data.get(bytes);

        try {
            RecordMessage recordMessage = JacksonToRecordSerializer.parse(objectMapper.readTree(bytes), this.topicSchemaMap.get(topic));
            this.topicSchemaMap.put(topic, recordMessage.getSchema());

            return recordMessage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
