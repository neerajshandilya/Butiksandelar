package se.atg.test.util;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Utils {

    public static LocalDate convertToLocalDate(@NonNull final Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertStringToDate(@NonNull final String dateToConvert) {
        LocalDate localDate = LocalDate.parse(dateToConvert);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
