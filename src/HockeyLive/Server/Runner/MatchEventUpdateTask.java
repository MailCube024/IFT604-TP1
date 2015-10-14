package HockeyLive.Server.Runner;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Server.Server;

import java.util.Random;
import java.util.TimerTask;

/**
 * Created by Michaël on 10/14/2015.
 */
public class MatchEventUpdateTask extends TimerTask {
    private final Server server;
    private final Random eventGenerator;

    public MatchEventUpdateTask(Server server) {
        this.server = server;
        eventGenerator = new Random();
    }

    @Override
    public void run() {
        server.LockForUpdate();
        for (Game g : server.GetNonCompletedGames()) {

            GameInfo info = server.GetMatchInfo(g);
            

        }
        server.UnlockUpdates();
    }
}
