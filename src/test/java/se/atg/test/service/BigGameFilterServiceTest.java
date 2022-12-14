package se.atg.test.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.atg.test.ButiksandelarApplication;
import se.atg.test.dto.GameEvent;
import se.atg.test.util.FixedClockConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static se.atg.test.util.TestUtils.getWinterBustGameEvents;

@SpringBootTest(classes = {ButiksandelarApplication.class, FixedClockConfig.class})
@RunWith(SpringRunner.class)
class BigGameFilterServiceTest {

    @Autowired
    private BigGameFilterService bigGameFilterService;


    @Test
    public void filterBigGameEventTestWithEmptyGameList() {
        var gameEvents = bigGameFilterService.filterBigGameEvent(Collections.emptyList());
        assertNotNull(gameEvents);
        assertEquals(0, gameEvents.size());
    }


    @Test
    public void filterBigGameEventTestWithWinterGamesList() {
        List<GameEvent> winterBustGameList = new ArrayList<>();
        winterBustGameList.add(getWinterBustGameEvents("2022-12-23", "V75"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-23", "V76"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-24", "V75"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-25", "V75"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-26", "V75"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-31", "V75"));

        var gameEvents = bigGameFilterService.filterBigGameEvent(winterBustGameList);
        assertEquals(4, gameEvents.size());
    }

    @Test
    public void filterBigGameEventTestWithOutWinterGamesList() {
        List<GameEvent> winterBustGameList = new ArrayList<>();
        winterBustGameList.add(getWinterBustGameEvents("2022-12-12", "V75"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-13", "V76"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-14", "V86"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-15", "V75"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-16", "V75"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-17", "V75"));
        winterBustGameList.add(getWinterBustGameEvents("2022-12-18", "GS75"));

        var gameEvents = bigGameFilterService.filterBigGameEvent(winterBustGameList);
        assertEquals(3, gameEvents.size());
    }


    @Test
    public void isBigWinWinterBurstGameTest() {
        assertTrue(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-23", "V75")));
        assertFalse(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-24", "V75")));
        assertTrue(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-25", "V75")));
        assertTrue(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-26", "V75")));
        assertFalse(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-27", "V75")));
        assertFalse(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-28", "V75")));
        assertFalse(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-29", "V75")));
        assertFalse(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-30", "V75")));
        assertTrue(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-31", "V75")));
        assertFalse(bigGameFilterService.isBigWinWinterBurstGame(getWinterBustGameEvents("2022-12-31", "V76")));
    }


    @Test
    public void isWinterBurstConditionApplyTest() {
        assertTrue(bigGameFilterService.isWinterBurstConditionApply(getWinterBustGameEvents("2022-12-23", "V75")));
        assertTrue(bigGameFilterService.isWinterBurstConditionApply(getWinterBustGameEvents("2022-12-25", "V75")));
        assertTrue(bigGameFilterService.isWinterBurstConditionApply(getWinterBustGameEvents("2022-12-28", "V75")));
        assertTrue(bigGameFilterService.isWinterBurstConditionApply(getWinterBustGameEvents("2022-12-30", "V75")));
        assertTrue(bigGameFilterService.isWinterBurstConditionApply(getWinterBustGameEvents("2022-12-31", "V75")));
        assertFalse(bigGameFilterService.isWinterBurstConditionApply(getWinterBustGameEvents("2022-12-22", "V75")));
    }

    @Test
    public void winterBurstConditionApplyAndGameDateisBigWindateTest() {
        assertTrue(bigGameFilterService.winterBurstConditionApplyAndGameDateisBigWindate(getWinterBustGameEvents("2022-12-23", "V75")));
        assertTrue(bigGameFilterService.winterBurstConditionApplyAndGameDateisBigWindate(getWinterBustGameEvents("2022-12-25", "V75")));
        assertFalse(bigGameFilterService.winterBurstConditionApplyAndGameDateisBigWindate(getWinterBustGameEvents("2022-12-23", "V76")));
        assertFalse(bigGameFilterService.winterBurstConditionApplyAndGameDateisBigWindate(getWinterBustGameEvents("2022-12-24", "V75")));
        assertTrue(bigGameFilterService.winterBurstConditionApplyAndGameDateisBigWindate(getWinterBustGameEvents("2022-12-26", "V75")));
        assertTrue(bigGameFilterService.winterBurstConditionApplyAndGameDateisBigWindate(getWinterBustGameEvents("2022-12-31", "V75")));
        assertFalse(bigGameFilterService.winterBurstConditionApplyAndGameDateisBigWindate(getWinterBustGameEvents("2022-12-31", "V70")));
        assertFalse(bigGameFilterService.winterBurstConditionApplyAndGameDateisBigWindate(getWinterBustGameEvents("2022-12-27", "V75")));
    }


}