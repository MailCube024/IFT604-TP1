package HockeyLive.Server.Runner;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Common.Models.Penalty;
import HockeyLive.Common.Models.Side;
import HockeyLive.Server.Server;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michaël on 10/14/2015.
 */
public class ChronometerUpdateTask implements Runnable {
    private final Server server;
    private final int TICK_VALUE = 30;

    public ChronometerUpdateTask(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        server.LockForUpdate();
        System.out.println("Obtained lock to update");
        for (Game g : server.GetNonCompletedGames()) {
            System.out.println("Altering " + g.toString());
            GameInfo info = server.GetGameInfo(g.getGameID());

            System.out.println("Chronometer before :" + info.getPeriodChronometer().getSeconds());
            info.decPeriodChronometer(Duration.ofSeconds(TICK_VALUE));
            System.out.println("Chronometer after :" + info.getPeriodChronometer().getSeconds());

            //Verify if we have completed a period
            Duration currentChronometer = info.getPeriodChronometer();
            if (currentChronometer.isZero() || currentChronometer.isNegative()) {
                // If chronometer is 0 and period is currently 3
                if (info.getPeriod() == 3) {
                    g.setCompleted(true);
                    System.out.println("Game " + g.toString() + " is completed - No more time");
                    info.setPeriodChronometer(Duration.ofMinutes(0));
                    synchronized (g) {
                        g.notifyAll();
                    }
                } else {
                    info.incPeriod();
                    System.out.println("Going to period (" + info.getPeriod() + ") for game " + g.toString());
                }
            }

            // If completed, clear all penalties, update and remove completed penalties otherwise
            if (g.isCompleted()) ClearPenalties(info);
            else UpdateAllPenalties(info);
        }
        System.out.println("Releasing lock to update");
        server.UnlockUpdates();
    }

    private void ClearPenalties(GameInfo info) {
        info.getVisitorPenalties().clear();
        info.getHostPenalties().clear();
    }

    private void UpdateAllPenalties(GameInfo info) {
        UpdatePenalties(info, Side.Host);
        UpdatePenalties(info, Side.Visitor);
    }

    private void UpdatePenalties(GameInfo info, Side side) {
        List<Penalty> toRemove = new ArrayList<>();
        for (Penalty p : info.getSidePenalties(side)) {
            p.decTimeLeft(Duration.ofSeconds(TICK_VALUE));
            Duration timeLeft = p.getTimeLeft();
            if (timeLeft.isZero() || timeLeft.isNegative())
                toRemove.add(p);
        }

        for (Penalty p : toRemove)
            info.removeSidePenalty(p, side);
    }

}
