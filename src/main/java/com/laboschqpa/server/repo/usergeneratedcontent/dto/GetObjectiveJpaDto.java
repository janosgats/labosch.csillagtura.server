package com.laboschqpa.server.repo.usergeneratedcontent.dto;

import com.laboschqpa.server.enums.ugc.ObjectiveType;

import java.time.Instant;

public interface GetObjectiveJpaDto extends GetUserGeneratedContentJpaDto {
    String getTitle();

    String getDescription();

    Boolean getSubmittable();

    Instant getDeadline();

    Instant getHideSubmissionsBefore();

    ObjectiveType getObjectiveType();
}