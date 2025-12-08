package com.redcare.challenge.provider.github;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GithubQueryTest {


    @Test
    public void shouldBuildGithubQuery() {
        LocalDate now = LocalDate.now();
        String lang = "Javascript";
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String expectedQuery = "language:Javascript created:>"+date;

        GithubQuery query = GithubQuery.builder()
                .createdDate(now)
                .language(lang)
                .build();

        assertEquals(expectedQuery, query.buildQueryString());

    }

}
