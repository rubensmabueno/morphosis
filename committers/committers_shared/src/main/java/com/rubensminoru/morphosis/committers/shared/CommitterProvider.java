package com.rubensminoru.morphosis.committers.shared;

import com.rubensminoru.morphosis.committers.shared.config.CommitterConfig;
import com.rubensminoru.morphosis.committers.shared.factories.CommitterFactory;

public interface CommitterProvider {
    public Class<? extends CommitterConfig> getCommitterConfigClass();
    public Class<? extends CommitterFactory> getCommitterFactoryClass();
    public String getName();
}
