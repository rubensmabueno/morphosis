package com.rubensminoru.morphosis.committers.shared;

import com.rubensminoru.morphosis.committers.shared.config.CommitterConfig;
import com.rubensminoru.morphosis.committers.shared.factories.CommitterFactory;

public interface CommitterProvider {
    Class<? extends CommitterConfig> getCommitterConfigClass();
    Class<? extends CommitterFactory> getCommitterFactoryClass();
    String getName();
}
