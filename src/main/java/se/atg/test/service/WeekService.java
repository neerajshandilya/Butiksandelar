package se.atg.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.atg.test.dto.ApplicationProp;
import se.atg.test.dto.GameEvent;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

@Component
public class WeekService {

    private final ApplicationProp applicationProp;
    private final Clock clock;

    @Autowired
    public WeekService(ApplicationProp applicationProp, Clock clock) {
        this.applicationProp = applicationProp;
        this.clock = clock;
    }


    boolean isBigGameEventType(@NotNull final GameEvent gameEvent) {
        var bigGameConfigurations = applicationProp.getBigGameConfigurations();
        final ApplicationProp.BigGameData bigGameData = new ApplicationProp.BigGameData();
        bigGameData.setType(gameEvent.getType());
        bigGameData.setDay(getDayString(gameEvent.getDate()));
        return bigGameConfigurations.contains(bigGameData);
    }

    Date getTodayDate() {
        return Date.from(ZonedDateTime.now(clock).toInstant());
    }

    void createFormattedString(@NotNull final StringBuffer out, int weekNo, @NotNull final List<GameEvent> sortedGamesList) {
        sortedGamesList.forEach(game -> {
            out.append(game.getType());
            out.append("(");
            out.append(getDayString(game.getDate()));
            if (weekNo > 0)
                out.append(" w").append(weekNo + 1);
            out.append(")");
            out.append("\n");
        });
    }

    String getDayString(@NotNull final Date date) {
        final DateFormat formatter = new SimpleDateFormat("EEEE", Locale.getDefault());
        return formatter.format(date);
    }


    int getWeekNumber(@NotNull final GameEvent gameEvent) {
        return getWeekNumber(gameEvent.getDate());
    }

    int getWeekNumber(@NotNull final Date inputDate) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(inputDate);
        return calendar.get(GregorianCalendar.WEEK_OF_YEAR);
    }

    int getWeekDiffFromTodayWeek(final GameEvent game) {
        return getWeekNumber(game) - getWeekNumber(getTodayDate());
    }
}
