package com.rubensminoru.morphosis.committers.s3_committer.config;

import com.rubensminoru.morphosis.committers.shared.config.CommitterConfig;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CommitterConfigImpl implements CommitterConfig {
    private String format;
}
