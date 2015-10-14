package HockeyLive.Server.Runner;

import HockeyLive.Server.Server;

import java.util.Timer;

/**
 * Created by Michaël on 10/14/2015.
 */
public class MatchEventUpdater {
    Timer timer;

    public MatchEventUpdater(int seconds, Server server) {
        timer = new Timer();
        timer.schedule(new MatchEventUpdateTask(server), seconds * 1000);
    }

    public void StopChronometer() {
        timer.cancel();
    }
}
