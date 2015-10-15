package HockeyLive.Server.Runner;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Common.Models.Goal;
import HockeyLive.Common.Models.Penalty;
import HockeyLive.Server.Factory.GameFactory;
import HockeyLive.Server.Server;

import java.util.TimerTask;

/**
 * Created by Michaël on 10/14/2015.
 */
public class MatchEventUpdateTask extends TimerTask {
    private final Server server;

    public MatchEventUpdateTask(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        server.LockForUpdate();
        for (Game g : server.GetNonCompletedGames()) {
            GameInfo info = server.GetMatchInfo(g);

            TryAddGoal(info);
            TryAddPenalty(info);

            //TODO: Do we create game to fill completed games?
        }
        server.UnlockUpdates();
    }

    private void TryAddGoal(GameInfo info) {
        Goal g = GameFactory.TryCreateGoal(info);
        if (g == null) return;

        //TODO: Created a goal for a team => Prepare a notification for Android client
    }

    private void TryAddPenalty(GameInfo info) {
        Penalty p = GameFactory.TryCreatePenalty(info);
        if (p == null) return;

        //TODO: Created a penalty for a team => Prepare a notification for Android client
    }
}
