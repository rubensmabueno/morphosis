package com.rubensminoru.morphosis.core.loaders;

import com.rubensminoru.morphosis.committers.shared.CommitterProvider;
import com.rubensminoru.morphosis.consumers.shared.ConsumerProvider;
import com.rubensminoru.morphosis.partitioners.shared.PartitionerProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
public class ConfigLoader {
    private List<ConsumerProviderLoader> consumerProviderLoader;

    public static ConfigLoader load(
            String fileName,
            List<ConsumerProvider> consumerProviderCatalog,
            List<PartitionerProvider> partitionerProviderCatalog,
            List<CommitterProvider> committerProviderCatalog
    ) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<ConsumerProviderLoader> consumerProviderLoader = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        InputStream resourceAsStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName);

        JsonNode jsonNode = mapper.readTree(resourceAsStream);

        for (JsonNode consumerNode : jsonNode.get("consumers")) {
            consumerProviderLoader.add(ConsumerProviderLoader.load(
                    consumerNode,
                    mapper,
                    consumerProviderCatalog,
                    partitionerProviderCatalog,
                    committerProviderCatalog
            ));
        }

        return new ConfigLoader(consumerProviderLoader);
    }
}
