package com.redcare.challenge.provider.algorithm;

import com.redcare.challenge.client.model.VersionControl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Service
public class ScoringPopularityService implements Function<VersionControl, Double> {

    @Value("${weight.fork:1}")
    private double forkWeight;

    @Value("${weight.star:1.5}")
    private double starWeight;

    @Value("${weight.update-date:1}")
    private double updateWeight;

    private final Clock clock;

    public ScoringPopularityService(Clock clock) {
        this.clock = clock;
    }


    @Override
    public Double apply(VersionControl versionControl) {
        LocalDateTime updatedAt = Instant.parse(versionControl.updatedAt())
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();

        long daysSinceUpdate = ChronoUnit.DAYS.between(updatedAt, LocalDateTime.now(clock));

        double weightedStar = starWeight * versionControl.stargazersCount();
        double weightedFork = forkWeight * versionControl.forksCount();
        double weightedUpdateVal =  updateWeight /(1+daysSinceUpdate);

        return weightedStar + weightedFork + weightedUpdateVal;
    }
}
