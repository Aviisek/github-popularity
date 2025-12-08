package com.redcare.challenge.provider.algorithm;

import com.redcare.challenge.client.model.VersionControl;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoringPopularityServiceTest {

    @Test
    public void testCalculatePopularity() {

        Instant fixedInstant = Instant.parse("2025-12-08T10:15:30Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        ScoringPopularityService popularityService = new ScoringPopularityService(fixedClock);
        ReflectionTestUtils.setField(popularityService, "forkWeight", 1d);
        ReflectionTestUtils.setField(popularityService, "starWeight", 1.5d);
        ReflectionTestUtils.setField(popularityService, "updateWeight", 1d);

        double popularityScore = popularityService.calculateScore(new VersionControl(
                "hello-algo",
                "krahets/hello-algo",
                "javascript",
                "https://github.com/krahets/hello-algo",
                "hello-algo",
                "2022-11-04T11:08:34Z",
                "2025-12-07T00:53:49Z",
                20L,
                30L
        ));

        assertEquals(65.5D, popularityScore);

    }
}
