package com.rubensminoru.morphosis.core.loaders;

import com.rubensminoru.morphosis.committers.shared.CommitterProvider;
import com.rubensminoru.morphosis.consumers.shared.ConsumerProvider;
import com.rubensminoru.morphosis.consumers.shared.config.ConsumerConfig;
import com.rubensminoru.morphosis.partitioners.shared.PartitionerProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
public class ConsumerProviderLoader {
    private ConsumerProvider consumerProvider;
    private ConsumerConfig consumerConfig;
    private List<PartitionerProviderLoader> partitionerProviderLoaderList;
    private List<CommitterProviderLoader> committerProviderLoaderList;

    public static ConsumerProviderLoader load(
            JsonNode node,
            ObjectMapper mapper,
            List<ConsumerProvider> consumerProviderCatalog,
            List<PartitionerProvider> partitionerProviderCatalog,
            List<CommitterProvider> committerProviderCatalog
    ) throws JsonProcessingException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<PartitionerProviderLoader> partitionerProviderLoaderList = new ArrayList<>();
        List<CommitterProviderLoader> committerProviderLoaderList = new ArrayList<>();

        for (ConsumerProvider consumerProvider : consumerProviderCatalog) {
            if (node.get("type").asText().equals(consumerProvider.getName())) {
                ConsumerConfig consumerConfig = mapper.treeToValue(node.get("consumer_config"), consumerProvider.getConsumerConfigClass());

                for (JsonNode partitionerNode : node.get("partitioners")) {
                    partitionerProviderLoaderList.add(
                            PartitionerProviderLoader.load(partitionerNode, mapper, partitionerProviderCatalog)
                    );
                }

                for (JsonNode committerNode : node.get("committers")) {
                    committerProviderLoaderList.add(
                            CommitterProviderLoader.load(committerNode, mapper, committerProviderCatalog)
                    );
                }

                return new ConsumerProviderLoader(
                        consumerProvider,
                        consumerConfig,
                        partitionerProviderLoaderList,
                        committerProviderLoaderList
                );
            }
        }

        throw new RuntimeException("Consumer of type " + node.get("type") + " aws not found.");
    }
}
