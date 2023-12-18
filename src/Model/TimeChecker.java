package Model;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeChecker {

    private ZonedDateTime upperTimeRange = null;
    private ZonedDateTime lowerTimeRange = null;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    public TimeChecker(){
    }

    public boolean needToFetchDataFromYesterday() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime twelveHoursAgo = now.minusHours(12);
        ZonedDateTime startOfToday = now.toLocalDate().atStartOfDay(now.getZone());
        return twelveHoursAgo.isBefore(startOfToday);
    }

    public String getTommorowDate(){
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        now = now.plusDays(1);
        return format.format(now);
    }


    public String getYesterdayDate(){
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        now = now.minusDays(1);
        return format.format(now);
    }


    public boolean needToFetchDataFromTomorrow() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime twelveHoursAfter = now.plusHours(12);
        ZonedDateTime startOfTomorrow = now.toLocalDate().plusDays(1).atStartOfDay(now.getZone());
        return twelveHoursAfter.isAfter(startOfTomorrow);
    }


    public void setTwelveHoursFromBackward() {
        ZonedDateTime now = ZonedDateTime.now();
        lowerTimeRange = now.minusHours(12);
    }


    public void setTwelveHoursAfterwards() {
        ZonedDateTime now = ZonedDateTime.now();
        upperTimeRange = now.plusHours(12);
    }

    ZonedDateTime getTwelveHoursFromBackward() {
        return lowerTimeRange;
    }

    ZonedDateTime getTwelveHoursFromAfterwards() {
        return upperTimeRange;
    }



}
