package collection.concurrency.service.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BeeperControl {

    public static void main(String[] args) {

        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        final Runnable beeper = () -> System.out.println("beep");

        final ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);

        scheduler.schedule(() -> {
            beeperHandle.cancel(true);
        }, 10, SECONDS);
    }

}
