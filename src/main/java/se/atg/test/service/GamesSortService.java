package se.atg.test.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.atg.test.dto.GameEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static se.atg.test.util.Utils.convertToLocalDate;


@Service
@Slf4j
public class GamesSortService {
    private final WeekService weekService;
    private final BigGameFilterService bigGameFilterService;


    //@Autowired
    public GamesSortService(WeekService weekService, BigGameFilterService bigGameFilterService) {
        this.weekService = weekService;
        this.bigGameFilterService = bigGameFilterService;
    }


    public List<String> sortGamesList(@NonNull final List<GameEvent> inputGamesList) {
        log.debug("inside sortGamesList processing gamesList {} with a size of {}", inputGamesList, inputGamesList.size());
        //Step(0) Sort all games as per their event date & discard any game that has event date in past
        final List<GameEvent> processGamesList = inputGamesList
                .parallelStream()
                .sorted(Comparator.comparing(GameEvent::getDate))
                .filter(this::gameDateAreInFuture)
                .toList();

        final List<String> processedList = new ArrayList<>();

        // key --> week_no , value --> GameEvents
        var weekNoGamesEventMap = processGamesList
                .parallelStream()
                .collect(Collectors.groupingBy(weekService::getWeekDiffFromTodayWeek));

        weekNoGamesEventMap
                .forEach((key, value) -> sortGamesListWeekly(value)
                        .stream()
                        .map(gameEvent -> weekService.createFormattedString(key, gameEvent))
                        .forEach(processedList::add));
        log.debug("sortGamesList after processing gamesList {} with a size of {}", processedList, processedList.size());
        return processedList;
    }

    private List<GameEvent> sortGamesListWeekly(@NonNull final List<GameEvent> inputGamesList) {
        log.debug("inside sortGamesListWeekly processing gamesList {} with a size of {}", inputGamesList, inputGamesList.size());
        //Step(1) create a processing List from input
        final List<GameEvent> processGamesList = new ArrayList<>(inputGamesList);

        //Step(2) Identify & Filter BigGame events from above sorted List
        final List<GameEvent> bigGamesList = bigGameFilterService.filterBigGameEvent(processGamesList);
        log.debug("filtered List of big games after processing processGamesList {} with a size of {}", bigGamesList, bigGamesList.size());
        processGamesList.removeAll(bigGamesList);

        //Step(3) find the index where sorted Big games can be added
        int indexToAddBigGameEvent = !processGamesList.isEmpty() ? findIndexToAddBigGameEvent(processGamesList.get(0)) : 0;
        log.trace("indexToAddBigGameEvent value is  {}", indexToAddBigGameEvent);
        //Step(4) add BigGames to the index found to the sortedList
        processGamesList.addAll(indexToAddBigGameEvent, bigGamesList);
        log.debug("sortGamesListWeekly after processing gamesList {} with a size of {}", processGamesList, processGamesList.size());
        return processGamesList.stream().toList();
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
