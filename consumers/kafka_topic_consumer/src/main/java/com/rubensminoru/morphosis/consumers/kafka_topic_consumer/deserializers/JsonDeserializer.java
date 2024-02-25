package com.rubensminoru.morphosis.consumers.kafka_topic_consumer.deserializers;

import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import com.rubensminoru.morphosis.shared.services.JacksonToRecordSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.ByteBuffer;


public class JsonDeserializer implements Deserializer<RecordMessage> {
    private final ObjectMapper objectMapper;

    public JsonDeserializer() {
        this.objectMapper = new ObjectMapper();
    }

    public RecordMessage deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            return JacksonToRecordSerializer.parse(objectMapper.readTree(data));
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
            return JacksonToRecordSerializer.parse(objectMapper.readTree(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
