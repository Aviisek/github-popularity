package com.redcare.challenge.client.model;

import java.util.List;

public record GithubVersionControlList(
        Integer totalCount,
        List<VersionControl> items
) { }
