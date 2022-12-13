package se.atg.test.util;

import se.atg.test.dto.GameEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TestUtils {

    public static GameEvent getWinterBustGameEvents(final String date, final String type) {
        GameEvent gameEvent = new GameEvent();
        gameEvent.setName("Junit_Winter_Burst_Game");
        gameEvent.setType(type);
        gameEvent.setDate(convertToDate(date));
        return gameEvent;
    }

    static Date convertToDate(final String dateToConvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateToConvert + " 11:20", formatter);
        return java.util.Date
                .from(dateTime.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Calendar getCalendarForagivenDate() {
        return new Calendar.Builder()
                .setDate(2022, Calendar.DECEMBER, 12)
                .setTimeOfDay(11, 05, 23)
                .build();
    }
}
