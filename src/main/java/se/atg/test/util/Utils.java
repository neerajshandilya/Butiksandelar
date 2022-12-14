package se.atg.test.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
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

    public static LocalDate convertStringToLocalDate(@NonNull final String dateToConvert) {
        return convertToLocalDate(convertStringToDate(dateToConvert));
    }
}
