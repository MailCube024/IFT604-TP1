package HockeyLive.Server.Runner;

import HockeyLive.Server.Server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michaël on 10/14/2015.
 */
public class Chronometer {
    private final ScheduledFuture<?> schedule;
    ChronometerUpdateTask task;
    private ScheduledExecutorService executor;

    public Chronometer(int seconds, Server server) {
        task = new ChronometerUpdateTask(server);
        executor = Executors.newSingleThreadScheduledExecutor();
        schedule = executor.scheduleWithFixedDelay(task, seconds, seconds, TimeUnit.SECONDS);
    }

    public void Stop() {
        schedule.cancel(true);
        executor.shutdownNow();
    }

}
