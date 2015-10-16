package HockeyLive.Client.Refresh;

import HockeyLive.Client.Communication.Client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michaël on 10/16/2015.
 */
public class GameInfoRefresher {
    private ScheduledFuture<?> schedule;
    private ScheduledExecutorService executor;
    private int selectedGameID = 0;
    private int interval;
    private Client client;

    public GameInfoRefresher(int minutes, Client client) {
        this.client = client;
        interval = minutes;
        executor = Executors.newSingleThreadScheduledExecutor();
        schedule = executor.scheduleWithFixedDelay(new GameInfoRefreshTask(client, selectedGameID), interval, interval, TimeUnit.MINUTES);
    }

    public void UpdateSelectedGame(int gameID) {
        selectedGameID = gameID;
        Reset();
    }

    public void Reset() {
        schedule.cancel(true);
        schedule = executor.scheduleWithFixedDelay(new GameInfoRefreshTask(client, selectedGameID), interval, interval, TimeUnit.MINUTES);
    }

    public void Stop() {
        schedule.cancel(true);
        executor.shutdownNow();
    }
}
