import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        // Example XML element content
        String starttimeutc = "2023-12-13T23:00:00Z";
        String endtimeutc = "2023-12-13T23:02:00Z";

        // Use the appropriate formatter for parsing
        DateTimeFormatter parser = DateTimeFormatter.ISO_DATE_TIME;
        ZonedDateTime startTime = ZonedDateTime.parse(starttimeutc, parser);
        ZonedDateTime endTime = ZonedDateTime.parse(endtimeutc, parser);

        // Formatter for output without 'T' and 'Z'
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Format the ZonedDateTime objects to strings
        String formattedStartTime = startTime.format(formatter);
        String formattedEndTime = endTime.format(formatter);

        System.out.println("Start Time (Local): " + formattedStartTime);
        System.out.println("End Time (Local): " + formattedEndTime);
    }
}
