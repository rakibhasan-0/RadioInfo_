import Model.XMLParser;
import org.junit.jupiter.api.Test;
import Model.*;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChannelParserTest{

    /**
     * check if we can fetch channels from the XML.
     */
    @Test
    public void testXMLParsing() throws Exception {
        XMLParser parser = new XMLParser();
        assertFalse(parser.getChannels().isEmpty());
    }


    /**
     * check if we can fetch channels from the xml correctly.
     */
    @Test
    public void checkIfAChannelGetParsed() throws Exception {
        // Sample XML content for channel "P1"
        String xmlContent = "<channel id=\"132\" name=\"P1\">"
                + "<color>31a1bd</color>"
                + "<tagline>den talade kanalen</tagline>"
                + "<siteurl>http://sverigesradio.se/p1</siteurl>"
                + "<liveaudio id=\"132\"><url>http://sverigesradio.se/topsy/direkt/132.mp3</url>"
                + "<statkey>webbradio/start/direkt/132_P1</statkey></liveaudio>"
                + "<scheduleurl>http://api.sr.se/api/v2/scheduledepisodes?channelid=132</scheduleurl>"
                + "<channeltype>Rikskanal</channeltype>"
                + "<xmltvid>p1.sr.se</xmltvid></channel>";

        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

        XMLParser parser = new XMLParser(mockConnection);
        Channel p1 = parser.getChannels().get(0);
        assertFalse(parser.getChannels().isEmpty(), "Channels list should not be empty");
        assertNotNull(p1, "Channel P1 should not be null");
        assertEquals("den talade kanalen", p1.getTagline(), "Tagline should match");
        assertEquals("Rikskanal", p1.getChannelType(), "Channel type should match");
    }




}
