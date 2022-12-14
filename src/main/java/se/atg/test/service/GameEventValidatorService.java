package se.atg.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import se.atg.test.dto.GameEvent;

import java.util.List;

import static se.atg.test.util.Utils.convertToLocalDate;

@Component("gameEventValidator")
@Slf4j
public class GameEventValidatorService implements Validator {

    private final WeekService weekService;
    private final BigGameFilterService bigGameFilterService;

    @Autowired
    public GameEventValidatorService(WeekService weekService, BigGameFilterService bigGameFilterService) {
        super();
        this.weekService = weekService;
        this.bigGameFilterService = bigGameFilterService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return GameEvent.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        List<GameEvent> gameEvents = (List<GameEvent>) target;
        var todayLocalDate = convertToLocalDate(weekService.getTodayDate());
        var gameEventListIterator = gameEvents.listIterator();
        while (gameEventListIterator.hasNext()) {
            var nextGameEvent = gameEventListIterator.next();
            var gameEventLocalDate = convertToLocalDate(nextGameEvent.getDate());
            if (gameEventLocalDate.isBefore(todayLocalDate)) {
                log.warn("invalid start date for the event {} on date {} will be discarded", nextGameEvent.getName(), nextGameEvent.getDate());
                gameEventListIterator.remove();
                // errors.rejectValue("startDate", "invalid start date for the event" + gameEvent.getName());
            } else if (bigGameFilterService.isWinterBurstConditionApply(nextGameEvent)) {
                log.debug("winter burst condition apply for event {} on date {}", nextGameEvent.getName(), nextGameEvent.getDate());
                if (!bigGameFilterService.isBigWinWinterBurstGame(nextGameEvent)) {
                    log.warn("During winterBurstGame condition doesn't matching for {} on date {}", nextGameEvent.getName(), nextGameEvent.getDate());
                    gameEventListIterator.remove();
                    // errors.rejectValue("gameType", "During winterBurstGame allowed game not matching" + gameEvent.getName());
                }

            }
        }
    }
}
