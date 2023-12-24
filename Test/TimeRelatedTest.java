import Controll.Controller;
import Model.*;
import View.MenuBarView;
import View.ProgramView;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeRelatedTest {


    /**
     * it will check if the fetched date and time falls within the specified range.
     */
    @Test
    public void testTimesWithinRange() {
        ZonedDateTime baseTime = ZonedDateTime.now();

        List<ZonedDateTime> times = new ArrayList<>();
        for (int i = -10; i <= 10; i++) {
            times.add(baseTime.plusHours(i));
        }

        TimeChecker timeChecker = new TimeChecker();
        timeChecker.setTwelveHoursAfterwards();
        timeChecker.setTwelveHoursFromBackward();

        ZonedDateTime lowerBound = timeChecker.getTwelveHoursFromBackward();
        ZonedDateTime upperBound = timeChecker.getTwelveHoursFromAfterwards();

        for (ZonedDateTime time : times) {
            assertTrue(time.isAfter(lowerBound) && time.isBefore(upperBound),
                    "The time " + time + " should be within the 12-hour range.");
        }
    }


    /**
     * Test if we are the time is wihtin the lower range.
     */
    @Test
    public void testTimeWithinLoweRange() {
        TimeChecker timeChecker = new TimeChecker();
        timeChecker.setTwelveHoursFromBackward();
        ZonedDateTime twelveHoursBackward = timeChecker.getTwelveHoursFromBackward();
        ZonedDateTime currentTimeMinusNineHours = ZonedDateTime.now().minusHours(9);
        assertTrue(twelveHoursBackward.isBefore(currentTimeMinusNineHours),
                "Current time minus 9 hours should be within the 12-hour range set by TimeChecker");
    }


    /**
     * Test if the time within the upper range.
     */
    @Test
    public void testTimeWithinUpperRange() {
        TimeChecker timeChecker = new TimeChecker();
        timeChecker.setTwelveHoursAfterwards();
        ZonedDateTime twelveHoursBackward = timeChecker.getTwelveHoursFromAfterwards();
        ZonedDateTime currentTime = ZonedDateTime.now().plusHours(9);
        assertTrue(twelveHoursBackward.isAfter(currentTime),
                "Current time plus 9 hours should be within the 12-hour range set by TimeChecker");
        currentTime = ZonedDateTime.now().plusDays(3);
        assertFalse(twelveHoursBackward.isAfter(currentTime), "Current time plus 3 days shouldnt be within the 12-hour range set by TimeChecker");
    }


    /**
     *  Tests that channel or schedules get udpated after one hour or a certain amount of time.
     */
    @Test
    public void testChannelAndScheduleUpdatedAfterOneHour() throws InterruptedException, InvocationTargetException {
        Controller controller = new Controller(new MenuBarView(), new ProgramView());
        CountDownLatch latch = new CountDownLatch(1);
        controller.setUpdateInterval(5 * 100);
        // let it wait for 10 seconds so that the backgorund thread is able to fetch data by using the backgorund thread.
        latch.await(10, TimeUnit.SECONDS);

        SwingUtilities.invokeLater(() -> {
            assertFalse(controller.getChannels().isEmpty(), "Channels should not be empty after update");
            controller.getChannels().clear();
            assertTrue(controller.getChannels().isEmpty(), "Channels should be empty");
        });
        // let it wait for 10 seconds so that the backgorund thread is able to fetch data by using the backgorund thread.
         latch.await(10, TimeUnit.SECONDS);

        SwingUtilities.invokeLater(() -> {
            assertFalse(controller.getChannels().isEmpty(), "Channels should not be empty after update");
        });

    }
}
