package com.redcare.challenge.domain.github;

import java.time.LocalDateTime;

public record GithubRepoWithScore(String name,
                                  String fullName,
                                  String language,
                                  String htmlUrl,
                                  String description,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt,
                                  Long forksCount,
                                  Long stargazersCount,
                                  Double popularityScore) {
}
