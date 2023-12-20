package Model;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is responsible for managing zoned time information. It maintains details about
 * the current date and time, as well as calculating dates and times for yesterday and tomorrow
 * relative to the current day.
 */
public class TimeChecker {
    private ZonedDateTime upperTimeRange = null;
    private ZonedDateTime lowerTimeRange = null;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    public TimeChecker(){
    }


    /**
     * It substracts the current date and time from 12 hours backwards, and checks
     * It exends to the yesterday relted to the current date and time.
     * @return true after subtracting 12 hours from the current date and time
     *         else it returns false.
     */
    public boolean needToFetchDataFromYesterday() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime twelveHoursAgo = now.minusHours(12);
        ZonedDateTime startOfToday = now.toLocalDate().atStartOfDay(now.getZone());
        return twelveHoursAgo.isBefore(startOfToday);
    }


    /**
     * It returns the tommorrow's day, which is related to the current date and time.
     * @return A string representing the day of the month and year format.
     */
    public String getTommorowDate(){
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        now = now.plusDays(1);
        return format.format(now);
    }



    /**
     * It returns the yesterday's day, which is related to the current date and time.
     * @return A string representing the day of the month and year format.
     */
    public String getYesterdayDate(){
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        now = now.minusDays(1);
        return format.format(now);
    }


    /**
     * It substracts the current date and time from 12 hours afterwards, and checks
     * It exends to the tommorow related to the current date and time.
     * @return true; after adding 12 hours from the current date and time it
     *          exceeds to tomorrow's date and time. else it returns false.
     */
    public boolean needToFetchDataFromTomorrow() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime twelveHoursAfter = now.plusHours(12);
        ZonedDateTime startOfTomorrow = now.toLocalDate().plusDays(1).atStartOfDay(now.getZone());
        return twelveHoursAfter.isAfter(startOfTomorrow);
    }


    /**
     * It set lower bound of the time which will be done by substraction 12 hours from the current
     * time and date.
     */
    public void setTwelveHoursFromBackward() {
        ZonedDateTime now = ZonedDateTime.now();
        lowerTimeRange = now.minusHours(12);
    }



    /**
     * It set upper bound of the time which will be done by adding 12 hours from the current
     * time and date.
     */
    public void setTwelveHoursAfterwards() {
        ZonedDateTime now = ZonedDateTime.now();
        upperTimeRange = now.plusHours(12);
    }

    /**
     * A getter method to get the lower bound of the time
     * @return the lower bound which represented in Zoned date and time format.
     */
    ZonedDateTime getTwelveHoursFromBackward() {
        return lowerTimeRange;
    }

    /**
     * A getter method to get the upper bound of the time
     * @return the upper bound which represented in Zoned date and time format.
     */
    ZonedDateTime getTwelveHoursFromAfterwards() {
        return upperTimeRange;
    }

}
