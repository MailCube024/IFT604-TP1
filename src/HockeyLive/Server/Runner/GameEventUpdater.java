package HockeyLive.Server.Runner;

import HockeyLive.Server.Server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michaël on 10/14/2015.
 */
public class GameEventUpdater {
    private final ScheduledFuture<?> schedule;
    GameEventUpdateTask task;
    private ScheduledExecutorService executor;

    public GameEventUpdater(int seconds, Server server) {
        task = new GameEventUpdateTask(server);
        executor = Executors.newSingleThreadScheduledExecutor();
        schedule = executor.scheduleWithFixedDelay(task, seconds, seconds, TimeUnit.SECONDS);
    }

    public void Stop() {
        schedule.cancel(true);
        executor.shutdownNow();
    }
}
