package com.rubensminoru.morphosis.shared.services;

import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import com.rubensminoru.morphosis.shared.entities.Schema;
import com.rubensminoru.morphosis.shared.entities.fields.Field;
import com.rubensminoru.morphosis.shared.entities.fields.IntegerField;
import com.rubensminoru.morphosis.shared.entities.fields.RecordField;
import com.rubensminoru.morphosis.shared.entities.fields.StringField;
import com.rubensminoru.morphosis.shared.entities.Pair;

import com.fasterxml.jackson.databind.JsonNode;
import com.rubensminoru.morphosis.shared.entities.values.IntegerValue;
import com.rubensminoru.morphosis.shared.entities.values.RecordValue;
import com.rubensminoru.morphosis.shared.entities.values.StringValue;
import com.rubensminoru.morphosis.shared.entities.values.Value;

import java.io.UnsupportedEncodingException;
import java.util.*;


public class JacksonToRecordSerializer {
    public static RecordMessage parse(JsonNode rootNode, Schema schema) throws UnsupportedEncodingException {
        Pair<Field, Value> message;

        if (schema != null) {
            message = parseField(rootNode, schema.getField());
        } else {
            message = buildRecordField(rootNode, null);
        }

        return new RecordMessage(
            new Schema(message.getLeft()),
            message.getRight()
        );
    }

    public static Value parseRecord(JsonNode jsonNode, RecordField field) {
        Set<Value> values = new LinkedHashSet<>();

        Map<String, JsonNode> jsonFields = convertToMap(jsonNode.fields());

        for (Field recordField : field.getFields()) {
            if (jsonNode.has(recordField.getName())) {
                JsonNode recordJsonNode = jsonFields.remove(recordField.getName());

                Pair<Field, Value> fieldValuePair = parseField(recordJsonNode, recordField);

                field.addField(fieldValuePair.getLeft());
                values.add(fieldValuePair.getRight());
            } else {
                Pair<Field, Value> fieldValuePair = parseField(recordField);

                field.addField(fieldValuePair.getLeft());
                values.add(fieldValuePair.getRight());
            }
        }

        if (!jsonFields.isEmpty()) {
            for (Map.Entry<String, JsonNode> jsonNodeEntry : jsonFields.entrySet()) {
                Pair<Field, Value> fieldValuePair = buildField(jsonNodeEntry.getValue(), jsonNodeEntry.getKey());

                field.addField(fieldValuePair.getLeft());
                values.add(fieldValuePair.getRight());
            }
        }

        return new RecordValue(values);
    }

    public static Pair<Field, Value> buildField(JsonNode jsonNode, String name) {
        if (jsonNode.isObject()) {
            return buildRecordField(jsonNode, name);
        } else if (jsonNode.isTextual()) {
            return new Pair<>(
                    new StringField(name),
                    new StringValue(jsonNode.asText())
            );
        } else if (jsonNode.isInt()) {
            return new Pair<>(
                    new IntegerField(name),
                    new IntegerValue(jsonNode.asInt())
            );
        }

        throw new RuntimeException();
    }

    public static Pair<Field, Value> buildRecordField(JsonNode jsonNode, String name) {
        Set<Field> fields = new LinkedHashSet<>();
        Set<Value> values = new LinkedHashSet<>();

        for (Map.Entry<String, JsonNode> jsonNodeEntry : convertToMap(jsonNode.fields()).entrySet()) {
            Pair<Field, Value> fieldValuePair = buildField(jsonNodeEntry.getValue(), jsonNodeEntry.getKey());

            fields.add(fieldValuePair.getLeft());
            values.add(fieldValuePair.getRight());
        }

        RecordField recordField;

        if (name != null) {
            recordField = new RecordField(name, fields);
        } else {
            recordField = new RecordField(fields);
        }

        return new Pair<>(
                recordField,
                new RecordValue(values)
        );
    }

    public static Pair<Field, Value> parseField(JsonNode jsonNode, Field field) {
        if (field instanceof StringField) {
            if (jsonNode.isTextual()) {
                return new Pair(
                        field,
                        new StringValue(jsonNode.asText())
                );
            }
        } else if (field instanceof IntegerField) {
            if (jsonNode.isInt()) {
                return new Pair(
                        field,
                        new IntegerValue(jsonNode.intValue())
                );
            }
        } else {
            if (jsonNode.isObject()) {
                return new Pair(
                        field,
                        parseRecord(jsonNode, (RecordField) field)
                );
            }
        }

        throw new RuntimeException();
    }

    public static Pair<Field, Value> parseField(Field field) {
        if (field instanceof StringField) {
            return new Pair(
                    field,
                    new StringValue(null)
            );
        } else if (field instanceof IntegerField) {
            return new Pair(
                    field,
                    new IntegerValue(null)
            );
        } else {
            return new Pair(
                    field,
                    new RecordValue(null)
            );
        }
    }

    public static <K, V> Map<K, V> convertToMap(Iterator<Map.Entry<K, V>> iterator) {
        Map<K, V> map = new HashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
