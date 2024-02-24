package com.rubensminoru.morphosis.core.loaders;

import com.rubensminoru.morphosis.partitioners.shared.PartitionerProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


@Getter
@AllArgsConstructor
public class PartitionerProviderLoader {
    private PartitionerProvider partitionerProvider;

    public static PartitionerProviderLoader load(JsonNode node, ObjectMapper mapper, List<PartitionerProvider> partitionerProviderCatalog) throws JsonProcessingException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (PartitionerProvider partitionerProvider : partitionerProviderCatalog) {
            if (node.get("type").asText().equals(partitionerProvider.getName())) {
                return new PartitionerProviderLoader(
                        partitionerProvider
                );
            }
        }

        throw new RuntimeException("Partitioner of type " + node.get("type") + " was not found.");
    }
}
