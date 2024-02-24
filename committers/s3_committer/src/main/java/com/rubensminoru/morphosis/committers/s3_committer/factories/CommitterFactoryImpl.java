package com.rubensminoru.morphosis.committers.s3_committer.factories;

import com.rubensminoru.morphosis.committers.s3_committer.CommitterImpl;
import com.rubensminoru.morphosis.committers.s3_committer.config.CommitterConfigImpl;
import com.rubensminoru.morphosis.committers.shared.Committer;
import com.rubensminoru.morphosis.committers.shared.factories.CommitterFactory;
import com.rubensminoru.morphosis.committers.shared.config.CommitterConfig;
import com.rubensminoru.morphosis.shared.entities.GenericEntity;

import java.io.IOException;

public class CommitterFactoryImpl extends GenericEntity implements CommitterFactory {
    private final CommitterConfigImpl config;

    public CommitterFactoryImpl(CommitterConfig config) {
        super();

        this.config = (CommitterConfigImpl) config;
    }

    @Override
    public Committer create() throws RuntimeException {
        if (this.config.getFormat().equals("parquet")) {
            try {
                return new CommitterImpl();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Format not supported: " + this.config.getFormat());
    }
}
