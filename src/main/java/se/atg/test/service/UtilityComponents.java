package se.atg.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@Slf4j
public class UtilityComponents {

    @Bean
    public Clock clock() {
       /* return Clock.fixed(
                Instant.parse("2022-12-12T10:05:23.653Z"),
                ZoneId.of("Europe/Prague"));*/
        return Clock.systemDefaultZone();
    }
}
