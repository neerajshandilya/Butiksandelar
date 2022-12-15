package se.atg.test.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.atg.test.dto.ApplicationProp;
import se.atg.test.dto.GameEvent;
import se.atg.test.util.Utils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static se.atg.test.util.Utils.convertToLocalDate;

@Service
@Slf4j
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
                .toList();
    }

    public List<GameEvent> filterBigGameEvent(@NonNull final List<GameEvent> inputGamesList) {
        log.debug("inside filterBigGameEvent processing gamesList {} with a size of {}", inputGamesList, inputGamesList.size());
        return inputGamesList
                .stream()
                .filter(this::gameDateAreInFuture)
                .filter(this::checkBigGameEvent)
                .sorted(Comparator.comparing(GameEvent::getDate))
                .toList();
    }

    private boolean checkBigGameEvent(GameEvent gameEvent) {
        return winterBurstConditionApplyAndGameDateisBigWindate(gameEvent) || weekService.isBigGameEventType(gameEvent);
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

    boolean gameDateAreInFuture(final GameEvent gameEvent) {
        var todayLocalDate = convertToLocalDate(weekService.getTodayDate());
        var gameEventLocalDate = convertToLocalDate(gameEvent.getDate());
        var gameFreeLocalDate = convertToLocalDate(winterburstConfiguration.getNoGameDate());
        return !gameFreeLocalDate.isEqual(gameEventLocalDate) && (gameEventLocalDate.isEqual(todayLocalDate) || gameEventLocalDate.isAfter(todayLocalDate));
    }


    boolean isBigWinWinterBurstGame(@NonNull final GameEvent gameEvent) {
        return winterBustBigGamesDate.contains(convertToLocalDate(gameEvent.getDate()))
                &&
                gameEvent.getType().equals(winterburstConfiguration.getAllowedGameType());
    }
}
