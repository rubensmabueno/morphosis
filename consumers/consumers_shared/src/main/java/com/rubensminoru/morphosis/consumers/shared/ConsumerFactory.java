package com.rubensminoru.morphosis.consumers.shared;

import com.rubensminoru.morphosis.consumers.shared.config.ConsumerConfig;

public interface ConsumerFactory {
    public Consumer create(ConsumerConfig config);
}
