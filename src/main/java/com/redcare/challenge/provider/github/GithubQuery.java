package com.redcare.challenge.provider.github;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GithubQuery {

    private final String language;
    private final LocalDate createdDate;

    public GithubQuery(Builder builder) {
        this.language = builder.language;
        this.createdDate = builder.createdDate;
    }

    public static Builder builder(){
        return new Builder();
    }


    public String buildQueryString() {
        StringBuilder query = new StringBuilder();

        boolean first = true;

        if (language != null && !language.isEmpty()) {
            query.append("language:").append(language);
            first = false;
        }

        if (createdDate != null) {
            if (!first) query.append(" ");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            query.append("created:>").append(dateTimeFormatter.format(createdDate));
        }

        return query.toString();
    }


    public static class Builder {
        private String language;
        private LocalDate createdDate;

        public Builder() {}

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder createdDate(LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public GithubQuery build(){
            return new GithubQuery(this);
        }
    }
}
