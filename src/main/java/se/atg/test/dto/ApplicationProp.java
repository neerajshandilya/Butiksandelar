package se.atg.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "application")
public class ApplicationProp {
    private List<BigGameData> bigGameConfigurations;

    private WinterburstConfiguration winterburstConfiguration;

    private String dateFormat;


    @Data
    @EqualsAndHashCode
    public static class BigGameData {
        private String type;
        private String day;
    }


    @Data
    @EqualsAndHashCode
    public static class WinterburstConfiguration {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private Date startDate;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private Date endDate;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private Date noGameDate;
        private String allowedGameType;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        //@DateTimeFormat(pattern = "yyyy-MM-dd")
        private List<String> bigGameDates;
    }

}
