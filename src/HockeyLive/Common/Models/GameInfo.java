package HockeyLive.Common.Models;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class GameInfo implements Serializable {
    private int GameID;
    private Duration PeriodChronometer;
    private int Period;
    private List<Penalty> HostPenalties;
    private List<Penalty> VisitorPenalties;
    private List<Goal> HostGoals;
    private List<Goal> VisitorGoals;
    private int HostGoalsTotal;
    private int VisitorGoalsTotal;
    private int periodLength;

    public GameInfo(int id, int periodLength) {
        this.GameID = id;
        this.PeriodChronometer = Duration.ofMinutes(periodLength);
        this.periodLength = periodLength;
        this.Period = 1;
        this.HostPenalties = new ArrayList<Penalty>();
        this.VisitorPenalties = new ArrayList<Penalty>();
        this.HostGoals = new ArrayList<Goal>();
        this.VisitorGoals = new ArrayList<Goal>();
        this.HostGoalsTotal = 0;
        this.VisitorGoalsTotal = 0;
    }

    public int getGameID() {
        return GameID;
    }

    public Duration getPeriodChronometer() {
        return PeriodChronometer;
    }

    public void setPeriodChronometer(Duration periodChronometer) {
        PeriodChronometer = periodChronometer;
    }

    public void incPeriodChronometer(Duration time) {
        PeriodChronometer = PeriodChronometer.plus(time);
    }

    public void decPeriodChronometer(Duration time) {
        PeriodChronometer = PeriodChronometer.minus(time);
    }

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int period) {
        Period = period;
    }

    public void incPeriod() {
        Period++;
        setPeriodChronometer(Duration.ofMinutes(periodLength));
    }

    public void decPeriod() {
        Period--;
    }

    public List<Penalty> getHostPenalties() {
        return HostPenalties;
    }

    public boolean addHostPenalties(Penalty p) {
        return HostPenalties.add(p);
    }

    public boolean removeHostPenalties(Penalty p) {
        return HostPenalties.remove(p);
    }

    public List<Penalty> getVisitorPenalties() {
        return VisitorPenalties;
    }

    private boolean removeVisitorPenalties(Penalty p) {
        return VisitorPenalties.remove(p);
    }

    public boolean addVisitorPenalties(Penalty p) {
        return VisitorPenalties.add(p);
    }

    public List<Goal> getHostGoals() {
        return HostGoals;
    }

    public boolean addHostGoals(Goal g) {
        for (Goal goal : HostGoals) {
            if (goal.getGoalHolder().equalsIgnoreCase(g.getGoalHolder())) {
                goal.incAmount();
                HostGoalsTotal++;
                return true;
            }
        }
        HostGoalsTotal++;
        return HostGoals.add(g);
    }

    public List<Goal> getVisitorGoals() {
        return VisitorGoals;
    }

    public boolean addVisitorGoals(Goal g) {
        for (Goal goal : VisitorGoals) {
            if (goal.getGoalHolder().equalsIgnoreCase(g.getGoalHolder())) {
                goal.incAmount();
                VisitorGoalsTotal++;
                return true;
            }
        }
        VisitorGoalsTotal++;
        return VisitorGoals.add(g);
    }

    public int getHostGoalsTotal() {
        return HostGoalsTotal;
    }

    public int getVisitorGoalsTotal() {
        return VisitorGoalsTotal;
    }

    public List<Goal> getSideGoals(Side side) {
        return (side == Side.Host) ? HostGoals : VisitorGoals;
    }

    public boolean addSideGoal(Goal g, Side side) {
        if (side == Side.Host) {
            return addHostGoals(g);
        } else {
            return addVisitorGoals(g);
        }
    }

    public void addSidePenalty(Penalty p, Side side) {
        if (side == Side.Host) addHostPenalties(p);
        else addVisitorPenalties(p);
    }

    public List<Penalty> getSidePenalties(Side side) {
        if (side == Side.Host) return getHostPenalties();
        else return getVisitorPenalties();
    }

    public boolean removeSidePenalty(Penalty p, Side side) {
        if (side == Side.Host) return removeHostPenalties(p);
        else return removeVisitorPenalties(p);
    }
}
