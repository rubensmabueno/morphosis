package com.rubensminoru.morphosis.core.loaders;

import com.rubensminoru.morphosis.committers.shared.CommitterProvider;
import com.rubensminoru.morphosis.committers.shared.config.CommitterConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class CommitterProviderLoader {
    private CommitterProvider committerProvider;
    private CommitterConfig committerConfig;

    public static CommitterProviderLoader load(
            JsonNode node,
            ObjectMapper mapper,
            List<CommitterProvider> committerProviderCatalog
    ) throws JsonProcessingException {
        for (CommitterProvider committerProvider : committerProviderCatalog) {
            if (node.get("type").asText().equals(committerProvider.getName())) {
                CommitterConfig committerConfig = mapper.treeToValue(node.get("config"), committerProvider.getCommitterConfigClass());

                return new CommitterProviderLoader(
                        committerProvider,
                        committerConfig
                );
            }
        }

        throw new RuntimeException("Committer of type " + node.get("type") + " was not found.");
    }
}
