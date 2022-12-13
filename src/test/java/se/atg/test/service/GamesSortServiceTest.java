package se.atg.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import se.atg.test.dto.GameEvent;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static se.atg.test.util.TestUtils.getWinterBustGameEvents;

@SpringBootTest
@RunWith(SpringRunner.class)
class GamesSortServiceTest {

    @MockBean
    private Clock clock;

    @Autowired
    private GamesSortService gamesSortService;

    @BeforeEach
    void setupClock() {
        when(clock.getZone()).thenReturn(
                ZoneId.of("Europe/Prague"));
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-04T10:05:23.653Z"));
    }

    @Test
    void testProcessGameListSampleSet1() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-05T10:05:23.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet1());
        assertEquals(
                "V64(Monday)\n" +
                        "V86(Wednesday)\n" +
                        "V75(Saturday)\n" +
                        "GS75(Sunday)\n" +
                        "V64(Tuesday)\n" +
                        "V64(Thursday)\n" +
                        "V86(Friday)\n" +
                        "V86(Wednesday w2)\n" +
                        "V75(Saturday w2)\n" +
                        "GS75(Sunday w2)\n" +
                        "V64(Monday w2)\n" +
                        "V64(Tuesday w2)\n" +
                        "V64(Thursday w2)\n" +
                        "GS75(Friday w2)\n", output);

    }


    @Test
    void testProcessGameListSampleSet2() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-06T00:00:00.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet1());
        assertEquals(
                "V64(Tuesday)\n" +
                        "V86(Wednesday)\n" +
                        "V75(Saturday)\n" +
                        "GS75(Sunday)\n" +
                        "V64(Thursday)\n" +
                        "V86(Friday)\n" +
                        "V86(Wednesday w2)\n" +
                        "V75(Saturday w2)\n" +
                        "GS75(Sunday w2)\n" +
                        "V64(Monday w2)\n" +
                        "V64(Tuesday w2)\n" +
                        "V64(Thursday w2)\n" +
                        "GS75(Friday w2)\n", output);

    }

    @Test
    void testProcessGameListSampleSet3() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-07T00:00:00.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet1());
        assertEquals(
                "V86(Wednesday)\n" +
                        "V75(Saturday)\n" +
                        "GS75(Sunday)\n" +
                        "V64(Thursday)\n" +
                        "V86(Friday)\n" +
                        "V86(Wednesday w2)\n" +
                        "V75(Saturday w2)\n" +
                        "GS75(Sunday w2)\n" +
                        "V64(Monday w2)\n" +
                        "V64(Tuesday w2)\n" +
                        "V64(Thursday w2)\n" +
                        "GS75(Friday w2)\n", output);

    }

    @Test
    void testProcessGameListDuringWinterBustPeriodGameSet1() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-19T00:00:00.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet2());
        assertEquals(
                "V64(Monday)\n" +
                        "V86(Wednesday)\n" +
                        "V75(Friday)\n" +
                        "V75(Sunday)\n" +
                        "V64(Tuesday)\n" +
                        "V64(Thursday)\n" +
                        "V75(Monday w2)\n" +
                        "V75(Saturday w2)\n" +
                        "V75(Tuesday w2)\n" +
                        "V75(Wednesday w2)\n" +
                        "V75(Thursday w2)\n" +
                        "V75(Friday w2)\n", output);

    }

    @Test
    void testProcessGameListDuringWinterBustPeriodGameSet2() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-20T00:00:00.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet2());
        assertEquals(
                "V64(Tuesday)\n" +
                        "V86(Wednesday)\n" +
                        "V75(Friday)\n" +
                        "V75(Sunday)\n" +
                        "V64(Thursday)\n" +
                        "V75(Monday w2)\n" +
                        "V75(Saturday w2)\n" +
                        "V75(Tuesday w2)\n" +
                        "V75(Wednesday w2)\n" +
                        "V75(Thursday w2)\n" +
                        "V75(Friday w2)\n", output);

    }

    @Test
    void testProcessGameListDuringWinterBustPeriodGameSet3() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-22T00:00:00.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet2());
        assertEquals(
                "V64(Thursday)\n" +
                        "V75(Friday)\n" +
                        "V75(Sunday)\n" +
                        "V75(Monday w2)\n" +
                        "V75(Saturday w2)\n" +
                        "V75(Tuesday w2)\n" +
                        "V75(Wednesday w2)\n" +
                        "V75(Thursday w2)\n" +
                        "V75(Friday w2)\n", output);

    }

    @Test
    void testProcessGameListDuringWinterBustPeriodGameSet4() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-23T00:00:00.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet2());
        assertEquals(
                "V75(Friday)\n" +
                        "V75(Sunday)\n" +
                        "V75(Monday w2)\n" +
                        "V75(Saturday w2)\n" +
                        "V75(Tuesday w2)\n" +
                        "V75(Wednesday w2)\n" +
                        "V75(Thursday w2)\n" +
                        "V75(Friday w2)\n", output);

    }

    @Test
    void testProcessGameListDuringWinterBustPeriodGameSet5() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-26T00:00:00.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet2());
        assertEquals(
                "V75(Monday)\n" +
                        "V75(Saturday)\n" +
                        "V75(Tuesday)\n" +
                        "V75(Wednesday)\n" +
                        "V75(Thursday)\n" +
                        "V75(Friday)\n", output);

    }

    @Test
    void testProcessGameListDuringWinterBustPeriodGameSet6() {
        when(clock.instant()).thenReturn(
                Instant.parse("2022-12-27T00:00:00.653Z"));
        var output = gamesSortService.processGameList(getGameDataSampleSet2());
        assertEquals(
                "V75(Tuesday)\n" +
                        "V75(Saturday)\n" +
                        "V75(Wednesday)\n" +
                        "V75(Thursday)\n" +
                        "V75(Friday)\n", output);

    }

    private List<GameEvent> getGameDataSampleSet1() {
        List<GameEvent> gameList = new ArrayList<>();
        gameList.add(getWinterBustGameEvents("2022-12-05", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-06", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-07", "V86"));
        gameList.add(getWinterBustGameEvents("2022-12-08", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-09", "V86"));
        gameList.add(getWinterBustGameEvents("2022-12-10", "V75"));
        gameList.add(getWinterBustGameEvents("2022-12-11", "GS75"));

        gameList.add(getWinterBustGameEvents("2022-12-12", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-13", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-14", "V86"));
        gameList.add(getWinterBustGameEvents("2022-12-15", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-16", "GS75"));
        gameList.add(getWinterBustGameEvents("2022-12-17", "V75"));
        gameList.add(getWinterBustGameEvents("2022-12-18", "GS75"));
        return gameList;
    }


    private List<GameEvent> getGameDataSampleSet2() {
        List<GameEvent> gameList = new ArrayList<>();

        gameList.add(getWinterBustGameEvents("2022-12-19", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-20", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-21", "V86"));
        gameList.add(getWinterBustGameEvents("2022-12-22", "V64"));
        gameList.add(getWinterBustGameEvents("2022-12-23", "V75"));
        gameList.add(getWinterBustGameEvents("2022-12-25", "V75"));

        gameList.add(getWinterBustGameEvents("2022-12-26", "V75"));
        gameList.add(getWinterBustGameEvents("2022-12-27", "V75"));
        gameList.add(getWinterBustGameEvents("2022-12-28", "V75"));
        gameList.add(getWinterBustGameEvents("2022-12-29", "V75"));
        gameList.add(getWinterBustGameEvents("2022-12-30", "V75"));
        gameList.add(getWinterBustGameEvents("2022-12-31", "V75"));
        return gameList;
    }
}