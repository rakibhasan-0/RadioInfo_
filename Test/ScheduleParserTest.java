import Model.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScheduleParserTest {


    @Test
    public void testParsingSingleScheduleProgram() throws Exception {

        ZonedDateTime currentTimeStart = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime currentTimeEnd = currentTimeStart.plusMinutes(30); // For example, 30 minutes later

        String startTime = "<starttimeutc>" + currentTimeStart.format(DateTimeFormatter.ISO_DATE_TIME) + "</starttimeutc>";
        String endTime = "<endtimeutc>" + currentTimeEnd.format(DateTimeFormatter.ISO_DATE_TIME) + "</endtimeutc>";

        String xmlContent = "<scheduledepisodes>"
                + "<scheduledepisode>"
                + "<episodeid>22441</episodeid>"
                + "<title>Ekonyheter</title>"
                + startTime
                + endTime
                +"<description>Hello World</description> "
                + "</scheduledepisode>"
                + "</scheduledepisodes>";


        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

        // Mock the channel to provide the mock URL
        Channel mockChannel = mock(Channel.class);
        when(mockChannel.getScheduleURL()).thenReturn("http://example.com/schedule");

        // Create an instance of ScheduleParser with the mocked channel
        ScheduleParser parser = new ScheduleParser(mockChannel);
        parser.fetchChannelScedule(mockConnection); // You might need to adjust this line based on your actual method signature

        // Verify that the schedules list is populated correctly
        assertFalse(parser.getScheduleList().isEmpty(), "Schedules list should not be empty");
        Schedule schedule = parser.getScheduleList().get(0);
        assertEquals("Ekonyheter", schedule.getProgramName(), "Program name should match");
        assertEquals("Hello World",schedule.getDescription(), "Description should match");
        // Add more assertions as needed
    }

    @Test
    public void testParsingEmptyOrMalformedXML() throws Exception {
        String xmlContent = "<scheduledepisodes></scheduledepisodes>"; // Empty schedule episodes

        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

        Channel mockChannel = mock(Channel.class);
        when(mockChannel.getScheduleURL()).thenReturn("http://example.com/schedule");

        ScheduleParser parser = new ScheduleParser(mockChannel);
        parser.fetchChannelScedule(mockConnection);

        assertTrue(parser.getScheduleList().isEmpty(), "Schedules list should be empty for empty XML");
    }


    @Test
    public void checkIfAChannelSchedulesGetParsed() throws Exception {
        Channel p3 = new ChannelBuilder()
                .setChannelName("p3")
                .setScheduleURL("http://api.sr.se/v2/scheduledepisodes?channelid=132")
                .setChannelType("Rikskanal")
                .build();

        ScheduleParser parser = new ScheduleParser(p3);
        assertFalse(parser.getScheduleList().isEmpty(), "Schedule list should not be empty");
    }


}
