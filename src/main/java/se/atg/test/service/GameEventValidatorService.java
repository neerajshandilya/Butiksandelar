package se.atg.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import se.atg.test.dto.GameEvent;

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
        GameEvent gameEvent = (GameEvent) target;
        var gameEventLocalDate = convertToLocalDate(gameEvent.getDate());
        var todayLocalDate = convertToLocalDate(weekService.getTodayDate());
        if (gameEventLocalDate.isBefore(todayLocalDate)) {
            log.warn("invalid start date for the event {}", gameEvent.getName());
            errors.rejectValue("startDate", "invalid start date for the event" + gameEvent.getName());
        }

        if (bigGameFilterService.isWinterBurstConditionApply(gameEvent)) {
            log.warn("winter burst condition apply for event {}", gameEvent.getName());
            if (!bigGameFilterService.isBigWinWinterBurstGame(gameEvent)) {
                log.warn("During winterBurstGame allowed game not matching for {}", gameEvent.getName());
                errors.rejectValue("gameType", "During winterBurstGame allowed game not matching" + gameEvent.getName());
            }

        }

    }
}
