package se.atg.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import se.atg.test.dto.GameEvent;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;


@Service
public class GamesSortService {
    private final WeekService weekService;
    private final BigGameFilterService bigGameFilterService;

    @Autowired
    public GamesSortService(WeekService weekService, BigGameFilterService bigGameFilterService) {
        this.weekService = weekService;
        this.bigGameFilterService = bigGameFilterService;
    }


    public String processGameList(@NotNull final List<GameEvent> games) {
        final StringBuffer out = new StringBuffer();
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
                    final List<GameEvent> sortedGamesList = weekNoGamesEventMap.get(weekNo);
                    processGamesListByWeek(sortedGamesList);
                    weekService.createFormattedString(out, weekNo, sortedGamesList);
                }
        );
        return out.toString();
    }

    private void processGamesListByWeek(@NotNull final List<GameEvent> sortedGamesList) {
        //Step(1) Sort all games as per their event date
        sortedGamesList.sort(Comparator.comparing(GameEvent::getDate));

        //Step(2) Identify & Filter BigGame events from above sorted List
        final List<GameEvent> bigGamesList = bigGameFilterService.filterBigGameEvent(sortedGamesList);

        //Step(3) Sort Big games according to their date
        bigGamesList.sort(Comparator.comparing(GameEvent::getDate));
        java.util.ListIterator<GameEvent> gameEventListIterator = sortedGamesList.listIterator();

        int indexToaddBigGameEvent = 0; //Index where big games event should be added
        while (gameEventListIterator.hasNext()) {
            var next = gameEventListIterator.next();
            if (next.getDate().before(weekService.getTodayDate())) {
                gameEventListIterator.remove();
            } else if (next.getDate().equals(weekService.getTodayDate())) {
                indexToaddBigGameEvent = sortedGamesList.indexOf(next) + 1;
                break;
            } else if (next.getDate().after(weekService.getTodayDate())) {
                indexToaddBigGameEvent = sortedGamesList.indexOf(next);
                break;
            }
        }
        //Step(4) add BigGames to the index found to the sortedList
        sortedGamesList.addAll(indexToaddBigGameEvent, bigGamesList);
    }

}
