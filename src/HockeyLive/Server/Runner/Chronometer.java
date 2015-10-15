package HockeyLive.Server.Runner;

import HockeyLive.Server.Server;

import java.util.Timer;

/**
 * Created by Michaël on 10/14/2015.
 */
public class Chronometer {
    Timer timer;

    public Chronometer(int seconds, Server server) {
        timer = new Timer();
        timer.schedule(new ChronometerUpdateTask(server), seconds * 1000);
    }

    public void Stop() {
        timer.cancel();
    }

}
