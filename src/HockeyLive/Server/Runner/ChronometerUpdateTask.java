package HockeyLive.Server.Runner;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Server.Server;

import java.time.Duration;
import java.util.TimerTask;

/**
 * Created by Michaël on 10/14/2015.
 */
public class ChronometerUpdateTask extends TimerTask {
    private final Server server;

    public ChronometerUpdateTask(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        for (Game g : server.GetNonCompletedGames()) {
            GameInfo info = server.GetMatchInfo(g);
            info.decPeriodChronometer(Duration.ofSeconds(30));

            //Verify if we have completed a period
            Duration currentChronometer = info.getPeriodChronometer();
            if (currentChronometer.isNegative() || currentChronometer.isZero()) {
                // If chronometer is 0 and period is currently 3
                if (info.getPeriod() == 3) g.setCompleted(true);
                else info.incPeriod();
            }
        }
    }
}
