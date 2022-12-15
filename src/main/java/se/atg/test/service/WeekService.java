package se.atg.test.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.atg.test.dto.ApplicationProp;
import se.atg.test.dto.GameEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@Slf4j
@Service
public class WeekService {

    private final ApplicationProp applicationProp;
    private final Clock clock;

    @Autowired
    public WeekService(ApplicationProp applicationProp, Clock clock) {
        this.applicationProp = applicationProp;
        this.clock = clock;
    }


    boolean isBigGameEventType(@NonNull final GameEvent gameEvent) {
        var bigGameConfigurations = applicationProp.getBigGameConfigurations();
        final ApplicationProp.BigGameData bigGameData = new ApplicationProp.BigGameData();
        bigGameData.setType(gameEvent.getType());
        bigGameData.setDay(getDayString(gameEvent.getDate()));
        return bigGameConfigurations.contains(bigGameData);
    }

    public Date getTodayDate() {
        return Date.from(ZonedDateTime.now(clock).toInstant());
    }

    public String createFormattedString(int weekNo, final GameEvent gameEvent) {
        final StringBuilder output = new StringBuilder();
        output.append(gameEvent.getType());
        output.append("(");
        output.append(getDayString(gameEvent.getDate()));
        if (weekNo > 0)
            output.append(" w").append(weekNo + 1);
        output.append(")");
        return output.toString();
    }

    String getDayString(@NonNull final Date date) {
        final DateFormat formatter = new SimpleDateFormat("EEEE", Locale.getDefault());
        return formatter.format(date);
    }


    int getWeekNumber(@NonNull final GameEvent gameEvent) {
        return getWeekNumber(gameEvent.getDate());
    }

    int getWeekNumber(@NonNull final Date inputDate) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(inputDate);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    int getWeekDiffFromTodayWeek(final GameEvent game) {
        return getWeekNumber(game) - getWeekNumber(getTodayDate());
    }


}
