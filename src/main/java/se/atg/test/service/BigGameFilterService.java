package se.atg.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.atg.test.dto.ApplicationProp;
import se.atg.test.dto.GameEvent;
import se.atg.test.util.Utils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static se.atg.test.util.Utils.convertToLocalDate;

@Service
public class BigGameFilterService {

    private final WeekService weekService;

    private final ApplicationProp.WinterburstConfiguration winterburstConfiguration;

    private final List<Date> winterBustBigGamesDate;


    @Autowired
    public BigGameFilterService(WeekService weekService, ApplicationProp applicationProp) {
        this.weekService = weekService;
        this.winterburstConfiguration = applicationProp.getWinterburstConfiguration();
        winterBustBigGamesDate = winterburstConfiguration
                .getBigGameDates()
                .stream()
                .map(Utils::convertStringToDate)
                .collect(Collectors.toList());
    }

    public List<GameEvent> filterBigGameEvent(@NotNull final List<GameEvent> sortedGamesList) {
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


    boolean winterBurstConditionApplyAndGameDateisBigWindate(@NotNull final GameEvent gameEvent) {
        return isWinterBurstConditionApply(gameEvent) && isBigWinWinterBurstGame(gameEvent);
    }

    boolean isWinterBurstConditionApply(@NotNull final GameEvent gameEvent) {
        var startDate = convertToLocalDate(winterburstConfiguration.getStartDate());
        var endDate = convertToLocalDate(winterburstConfiguration.getEndDate());
        var gameDate = convertToLocalDate(gameEvent.getDate());

        return (gameDate.equals(startDate) || gameDate.equals(endDate))
                || (gameDate.isBefore(endDate)) && gameDate.isAfter(startDate);
    }


    boolean isBigWinWinterBurstGame(@NotNull final GameEvent gameEvent) {
        return winterBustBigGamesDate.contains(gameEvent.getDate())
                &&
                gameEvent.getType().equals(winterburstConfiguration.getAllowedGameType());
    }


}
