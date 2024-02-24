package com.rubensminoru.morphosis.core.loaders;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;


@Getter
@AllArgsConstructor
public class ProviderCatalogLoader<T> {
    private List<T> providerCatalog;

    public static <T> ProviderCatalogLoader<T> load(Class<T> providerClass) {
        ServiceLoader<T> loader = ServiceLoader.load(providerClass);

        List<T> providers = new ArrayList<>();

        for (T provider : loader) {
            providers.add(provider);
        }

        return new ProviderCatalogLoader<>(providers);
    }
}