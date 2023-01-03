package se.atg.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import se.atg.test.dto.GameEvent;

import java.util.List;

import static se.atg.test.util.Utils.convertToLocalDate;

@Component("gameEventValidator")
@Slf4j
public class GameEventValidator implements Validator {

    private final WeekService weekService;
    private final BigGameFilterService bigGameFilterService;

    //@Autowired
    public GameEventValidator(WeekService weekService, BigGameFilterService bigGameFilterService) {
        super();
        this.weekService = weekService;
        this.bigGameFilterService = bigGameFilterService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return GameEvent.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        List<GameEvent> inputGamesList = (List<GameEvent>) target;
        log.debug("processing gamesList {} with a size of {}", inputGamesList, inputGamesList.size());
        inputGamesList.removeIf(this::validateGamesDate);
        log.debug("after processing gamesList {} with a size of {}", inputGamesList, inputGamesList.size());
    }

    private boolean validateGamesDate(final GameEvent nextGameEvent) {
        boolean result = false;
        var todayLocalDate = convertToLocalDate(weekService.getTodayDate());
        var gameEventLocalDate = convertToLocalDate(nextGameEvent.getDate());
        if (gameEventLocalDate.isBefore(todayLocalDate)) {
            result = true;
            log.warn("invalid start date for the event {} on date {} will be discarded", nextGameEvent.getName(), nextGameEvent.getDate());
        } else if (bigGameFilterService.isWinterBurstConditionApply(nextGameEvent)) {
            log.debug("winter burst condition apply for event {} on date {}", nextGameEvent.getName(), nextGameEvent.getDate());
            if (!bigGameFilterService.isBigWinWinterBurstGame(nextGameEvent)) {
                result = true;
                log.warn("During winterBurstGame condition doesn't matching for {} on date {}", nextGameEvent.getName(), nextGameEvent.getDate());
            }
        }
        return result;
    }
}
