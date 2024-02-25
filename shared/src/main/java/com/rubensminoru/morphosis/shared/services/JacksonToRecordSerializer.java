package com.rubensminoru.morphosis.shared.services;

import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import com.rubensminoru.morphosis.shared.entities.Schema;
import com.rubensminoru.morphosis.shared.entities.fields.Field;
import com.rubensminoru.morphosis.shared.entities.fields.IntegerField;
import com.rubensminoru.morphosis.shared.entities.fields.RecordField;
import com.rubensminoru.morphosis.shared.entities.fields.StringField;

import com.fasterxml.jackson.databind.JsonNode;
import com.rubensminoru.morphosis.shared.entities.values.IntegerValue;
import com.rubensminoru.morphosis.shared.entities.values.RecordValue;
import com.rubensminoru.morphosis.shared.entities.values.StringValue;
import com.rubensminoru.morphosis.shared.entities.values.Value;

import java.io.UnsupportedEncodingException;
import java.util.*;


public class JacksonToRecordSerializer {
    public static RecordMessage parse(JsonNode rootNode) throws UnsupportedEncodingException {
        List<Field> fields = new ArrayList<>();
        LinkedHashMap<Field, Value> fieldValues = new LinkedHashMap<>();

        for (Iterator<Map.Entry<String, JsonNode>> it = rootNode.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> jsonNodeEntry = it.next();
            Field field;
            Value value;

            if (jsonNodeEntry.getValue().isTextual()) {
                field = new StringField(jsonNodeEntry.getKey());
                value = new StringValue(jsonNodeEntry.getValue().textValue());
            } else if (jsonNodeEntry.getValue().isInt()) {
                field = new IntegerField(jsonNodeEntry.getKey());
                value = new IntegerValue(jsonNodeEntry.getValue().intValue());
            } else {
                field = new StringField(jsonNodeEntry.getKey());
                value = new StringValue(jsonNodeEntry.getValue().textValue());
            }

            fields.add(field);
            fieldValues.put(field, value);
        }

        return new RecordMessage(
            new Schema(new RecordField(fields)),
            new RecordValue(fieldValues)
        );
    }
}
