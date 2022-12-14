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
import java.util.Optional;

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


    public List<String> processGameList(@NonNull final List<GameEvent> games) {
        final List<String> processedList = new ArrayList<>();
        // key --> week_no , value --> GameEvents
        final MultiValueMap<Integer, GameEvent> weekNoGamesEventMap = new LinkedMultiValueMap<>();
        games.forEach(
                game -> {
                    var diff = weekService.getWeekDiffFromTodayWeek(game);
                    weekNoGamesEventMap.add(diff, game);
                }
        );

        weekNoGamesEventMap.keySet().forEach(
                weekNo -> {
                    var gameEvents = Optional.ofNullable(weekNoGamesEventMap.get(weekNo));
                    if (gameEvents.isPresent()) {
                        var processGamesListByWeek = processGamesListByWeek(gameEvents.get());
                        processGamesListByWeek.forEach(
                                gameEvent ->
                                {
                                    var formattedString = weekService.createFormattedString(weekNo, gameEvent);
                                    processedList.add(formattedString);
                                }
                        );
                    }
                }
        );
        return processedList;
    }

    private List<GameEvent> processGamesListByWeek(@NonNull final List<GameEvent> sortedGamesList) {
        //Step(1) Sort all games as per their event date
        sortedGamesList.sort(Comparator.comparing(GameEvent::getDate));

        //Step(2) Identify & Filter BigGame events from above sorted List
        final List<GameEvent> bigGamesList = bigGameFilterService.filterBigGameEvent(sortedGamesList);

        //Step(3) Sort Big games according to their date
        bigGamesList.sort(Comparator.comparing(GameEvent::getDate));
        java.util.ListIterator<GameEvent> gameEventListIterator = sortedGamesList.listIterator();

        int indexToaddBigGameEvent = 0; //Index where big games event should be added
        while (gameEventListIterator.hasNext()) {
            var gameEvent = gameEventListIterator.next();
            var todayLocalDate = convertToLocalDate(weekService.getTodayDate());
            var gameEventLocalDate = convertToLocalDate(gameEvent.getDate());
            if (gameEventLocalDate.isBefore(todayLocalDate)) {
                gameEventListIterator.remove();
            } else if (gameEventLocalDate.isEqual(todayLocalDate)) {
                indexToaddBigGameEvent = sortedGamesList.indexOf(gameEvent) + 1;
                break;
            } else if (gameEventLocalDate.isAfter(todayLocalDate)) {
                indexToaddBigGameEvent = sortedGamesList.indexOf(gameEvent);
                break;
            }
        }
        //Step(4) add BigGames to the index found to the sortedList
        sortedGamesList.addAll(indexToaddBigGameEvent, bigGamesList);
        return sortedGamesList;
    }
}
