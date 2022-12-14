package se.atg.test.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.atg.test.dto.ApplicationProp;
import se.atg.test.dto.GameEvent;
import se.atg.test.util.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static se.atg.test.util.Utils.convertToLocalDate;

@Service
public class BigGameFilterService {

    private final WeekService weekService;

    private final ApplicationProp.WinterburstConfiguration winterburstConfiguration;

    private final List<LocalDate> winterBustBigGamesDate;


    @Autowired
    public BigGameFilterService(WeekService weekService, ApplicationProp applicationProp) {
        this.weekService = weekService;
        this.winterburstConfiguration = applicationProp.getWinterburstConfiguration();
        winterBustBigGamesDate = winterburstConfiguration
                .getBigGameDates()
                .stream()
                .map(Utils::convertStringToLocalDate)
                .collect(Collectors.toList());
    }

    public List<GameEvent> filterBigGameEvent(@NonNull final List<GameEvent> sortedGamesList) {
        final List<GameEvent> bigGamesList = new ArrayList<>();
        var gameEventListIterator = sortedGamesList.listIterator();
        while (gameEventListIterator.hasNext()) {
            var gameEvent = gameEventListIterator.next();

            var todayLocalDate = convertToLocalDate(weekService.getTodayDate());
            var gameEventLocalDate = convertToLocalDate(gameEvent.getDate());
            if (gameEventLocalDate.isBefore(todayLocalDate)) {
                gameEventListIterator.remove();
            } else if (isWinterBurstConditionApply(gameEvent)) {
                if (isBigWinWinterBurstGame(gameEvent)) {
                    bigGamesList.add(gameEvent);
                    gameEventListIterator.remove();
                }
            } else if (weekService.isBigGameEventType(gameEvent)) {
                bigGamesList.add(gameEvent);
                gameEventListIterator.remove();
            }

        }
        return bigGamesList;
    }


    boolean winterBurstConditionApplyAndGameDateisBigWindate(@NonNull final GameEvent gameEvent) {
        return isWinterBurstConditionApply(gameEvent) && isBigWinWinterBurstGame(gameEvent);
    }

    boolean isWinterBurstConditionApply(@NonNull final GameEvent gameEvent) {
        var startDate = convertToLocalDate(winterburstConfiguration.getStartDate());
        var endDate = convertToLocalDate(winterburstConfiguration.getEndDate());
        var gameDate = convertToLocalDate(gameEvent.getDate());

        return (gameDate.equals(startDate) || gameDate.equals(endDate))
                || (gameDate.isBefore(endDate)) && gameDate.isAfter(startDate);
    }


    boolean isBigWinWinterBurstGame(@NonNull final GameEvent gameEvent) {
        return winterBustBigGamesDate.contains(convertToLocalDate(gameEvent.getDate()))
                &&
                gameEvent.getType().equals(winterburstConfiguration.getAllowedGameType());
    }


}
