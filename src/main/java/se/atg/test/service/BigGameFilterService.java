package se.atg.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.atg.test.dto.ApplicationProp;
import se.atg.test.dto.GameEvent;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static se.atg.test.util.Utils.convertToDate;

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
                .map(BigGameFilterService::convertStringToDate)
                .collect(Collectors.toList());
    }

    public List<GameEvent> filterBigGameEvent(@NotNull final List<GameEvent> sortedGamesList) {
        final List<GameEvent> bigGamesList = new ArrayList<>();
        var gameEventListIterator = sortedGamesList.listIterator();
        while (gameEventListIterator.hasNext()) {
            var gameEvent = gameEventListIterator.next();
            if (isWinterBurstConditionApply(gameEvent)) {
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
        var startDate = convertToDate(winterburstConfiguration.getStartDate());
        var endDate = convertToDate(winterburstConfiguration.getEndDate());
        var gameDate = convertToDate(gameEvent.getDate());

        return (gameDate.equals(startDate) || gameDate.equals(endDate))
                || (gameDate.isBefore(endDate)) && gameDate.isAfter(startDate);
    }


    boolean isBigWinWinterBurstGame(@NotNull final GameEvent gameEvent) {
        return winterBustBigGamesDate.contains(gameEvent.getDate())
                &&
                gameEvent.getType().equals(winterburstConfiguration.getAllowedGameType());
    }


    private static Date convertStringToDate(@NotNull final String dateToConvert) {
        LocalDate localDate = LocalDate.parse(dateToConvert);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


}
