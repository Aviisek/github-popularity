package com.redcare.challenge.client.model;

public record VersionControl(String name,
                             String fullName,
                             String language,
                             String htmlUrl,
                             String description,
                             String createdAt,
                             String updatedAt,
                             Long forksCount,
                             Long stargazersCount) {
}
