package pricer.service;
import java.text.SimpleDateFormat;

public interface StaticParams {
    Integer physicalDays = 365;
    Integer tradingDays = 252;
    Integer tradingHours = 8;
    Integer minutesHour = 60;

    SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
}
