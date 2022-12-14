package se.atg.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import se.atg.test.dto.ApplicationProp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Component
public class UtilityComponents {

    private final ApplicationProp applicationProp;

    @Autowired
    public UtilityComponents(ApplicationProp applicationProp) {
        this.applicationProp = applicationProp;
    }

    @Bean
    public Clock clock() {
        return Clock.fixed(
                Instant.parse("2022-12-12T10:05:23.653Z"),
                ZoneId.of("Europe/Prague"));
        //return Clock.systemDefaultZone();
    }

    @Primary
    @Bean
    public DateFormat getDateFormat() {
        return new SimpleDateFormat(applicationProp.getDateFormat());
    }
}
