package se.atg.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static se.atg.test.util.TestUtils.createGameEvents;
import static se.atg.test.util.TestUtils.getCalendarForagivenFixDate;
import static se.atg.test.util.Utils.convertStringToDate;
import static se.atg.test.util.Utils.convertToLocalDate;

@SpringBootTest
@RunWith(SpringRunner.class)
class WeekServiceTest {
    @Autowired
    private WeekService weekService;


    @MockBean
    private Clock clock;

    @BeforeEach
    void setupClock() {
        when(clock.getZone()).thenReturn(
                ZoneId.of("Europe/Prague"));
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-12T10:05:23.653Z"));
    }

    @Test
    void testGetTodayDate() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-12T10:05:23.653Z"));
        var localDateFromApi = convertToLocalDate(weekService.getTodayDate());
        var expectedLocalDate = convertToLocalDate(convertStringToDate("2022-12-12"));
        assertTrue(localDateFromApi.isEqual(expectedLocalDate));
    }


    @Test
    void testGetDayString() {
        Calendar calendar = getCalendarForagivenFixDate();
        assertEquals("Monday", weekService.getDayString(calendar.getTime()));
    }

    @Test
    void testGetWeekNumber() {
        Calendar calendar = getCalendarForagivenFixDate();
        assertEquals(50, weekService.getWeekNumber(calendar.getTime()));
    }

    @Test
    void testCreateFormattedString() {
        assertEquals("V64(Monday w2)", weekService.createFormattedString(1, createGameEvents("2022-12-19", "V64")));
        assertEquals("V64(Monday)", weekService.createFormattedString(0, createGameEvents("2022-12-19", "V64")));
    }

    @Test
    void testGetWeekDiffFromTodayWeek() {
        var winterBustGameEvents = createGameEvents("2022-12-12", "V76");
        assertEquals(0, weekService.getWeekDiffFromTodayWeek(winterBustGameEvents));
        winterBustGameEvents = createGameEvents("2022-12-19", "V76");
        assertEquals(1, weekService.getWeekDiffFromTodayWeek(winterBustGameEvents));
        winterBustGameEvents = createGameEvents("2022-12-18", "V76");
        assertEquals(0, weekService.getWeekDiffFromTodayWeek(winterBustGameEvents));
    }

    @Test
    void testIsBigGameEventType() {
        var winterBustGameEvents = createGameEvents("2022-12-12", "V76");
        assertFalse(weekService.isBigGameEventType(winterBustGameEvents));
        winterBustGameEvents = createGameEvents("2022-12-14", "V86");
        assertTrue(weekService.isBigGameEventType(winterBustGameEvents));
    }


}