package se.atg.test.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import se.atg.test.dto.GameEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static se.atg.test.util.Utils.convertToLocalDate;


@Service
@Slf4j
public class GamesSortService {
    private final WeekService weekService;
    private final BigGameFilterService bigGameFilterService;


    @Autowired
    public GamesSortService(WeekService weekService, BigGameFilterService bigGameFilterService) {
        this.weekService = weekService;
        this.bigGameFilterService = bigGameFilterService;
    }


    public List<String> sortGamesList(@NonNull final List<GameEvent> inputGamesList) {
        //Step(0) Sort all games as per their event date & discard any game that has event date in past
        final List<GameEvent> processGamesList = inputGamesList
                .stream()
                .sorted(Comparator.comparing(GameEvent::getDate))
                .filter(this::gameDateAreInFuture)
                .toList();

        final List<String> processedList = new ArrayList<>();
        // key --> week_no , value --> GameEvents
        final MultiValueMap<Integer, GameEvent> weekNoGamesEventMap = new LinkedMultiValueMap<>();
        for (GameEvent game : processGamesList)
            weekNoGamesEventMap.add(weekService.getWeekDiffFromTodayWeek(game), game);

        for (Map.Entry<Integer, List<GameEvent>> entry : weekNoGamesEventMap.entrySet()) {
            for (GameEvent gameEvent : sortGamesListWeekly(entry.getValue())) {
                processedList.add(weekService.createFormattedString(entry.getKey(), gameEvent));
            }
        }

        return processedList;
    }

    private List<GameEvent> sortGamesListWeekly(@NonNull final List<GameEvent> inputGamesList) {
        //Step(1) create a processing List from input
        final List<GameEvent> processGamesList = new ArrayList<>(inputGamesList);

        //Step(2) Identify & Filter BigGame events from above sorted List
        final List<GameEvent> bigGamesList = bigGameFilterService.filterBigGameEvent(processGamesList);
        processGamesList.removeAll(bigGamesList);

        //Step(3) find the index where sorted Big games can be added
        int indexToAddBigGameEvent = !processGamesList.isEmpty() ? findIndexToAddBigGameEvent(processGamesList.get(0)) : 0;

        //Step(4) add BigGames to the index found to the sortedList
        processGamesList.addAll(indexToAddBigGameEvent, bigGamesList);
        return processGamesList;
    }

    int findIndexToAddBigGameEvent(final GameEvent gameEvent) {
        var todayLocalDate = convertToLocalDate(weekService.getTodayDate());
        var gameEventLocalDate = convertToLocalDate(gameEvent.getDate());
        return (gameEventLocalDate.isEqual(todayLocalDate)) ? 1 : 0;
    }


    boolean gameDateAreInFuture(final GameEvent gameEvent) {
        var todayLocalDate = convertToLocalDate(weekService.getTodayDate());
        var gameEventLocalDate = convertToLocalDate(gameEvent.getDate());
        return gameEventLocalDate.isEqual(todayLocalDate) || gameEventLocalDate.isAfter(todayLocalDate);
    }
}
