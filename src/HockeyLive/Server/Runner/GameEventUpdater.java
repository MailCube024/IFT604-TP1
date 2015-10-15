package HockeyLive.Server.Runner;

import HockeyLive.Server.Server;

import java.util.Timer;

/**
 * Created by Michaël on 10/14/2015.
 */
public class GameEventUpdater {
    Timer timer;

    public GameEventUpdater(int seconds, Server server) {
        timer = new Timer();
        timer.schedule(new GameEventUpdateTask(server), seconds * 1000);
    }

    public void Stop() {
        timer.cancel();
    }
}
