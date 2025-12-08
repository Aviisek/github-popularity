package com.redcare.challenge.domain.github;


import java.util.List;


public record GithubRepoWithScoreList(
        long total,
        List<GithubRepoWithScore> items,
        long currentPage,
        long maxPage) { }
