package com.rubensminoru.morphosis.committers.s3_committer;

import com.rubensminoru.morphosis.committers.s3_committer.config.CommitterConfigImpl;
import com.rubensminoru.morphosis.committers.s3_committer.factories.CommitterFactoryImpl;
import com.rubensminoru.morphosis.committers.shared.factories.CommitterFactory;
import com.rubensminoru.morphosis.committers.shared.CommitterProvider;
import com.rubensminoru.morphosis.committers.shared.config.CommitterConfig;

public class CommitterProviderImpl implements CommitterProvider {
    @Override
    public Class<? extends CommitterConfig> getCommitterConfigClass() {
        return CommitterConfigImpl.class;
    }

    @Override
    public Class<? extends CommitterFactory> getCommitterFactoryClass() {
        return CommitterFactoryImpl.class;
    }

    @Override
    public String getName() {
        return "s3";
    }
}

