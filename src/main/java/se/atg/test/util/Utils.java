package se.atg.test.util;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Utils {

    public static LocalDate convertToLocalDate(@NotNull final Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertStringToDate(@NotNull final String dateToConvert) {
        LocalDate localDate = LocalDate.parse(dateToConvert);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
