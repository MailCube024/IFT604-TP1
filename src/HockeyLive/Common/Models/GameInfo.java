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

    public void setHostPenalties(List<Penalty> hostPenalties) {
        HostPenalties = hostPenalties;
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

    public void setVisitorPenalties(List<Penalty> visitorPenalties) {
        VisitorPenalties = visitorPenalties;
    }

    public boolean addVisitorPenalties(Penalty p) {
        return VisitorPenalties.add(p);
    }

    public boolean removeVisitorPenalties(Penalty p) {
        return VisitorPenalties.remove(p);
    }

    public List<Goal> getHostGoals() {
        return HostGoals;
    }

    public void setHostGoals(List<Goal> hostGoals) {
        HostGoals = hostGoals;
    }

    public boolean addHostGoals(Goal g) {
        for(Goal goal : HostGoals){
            if(goal.getGoalHolder().equals(g.getGoalHolder())){
                goal.incAmount();
                this.HostGoalsTotal++;
                return true;
            }
        }
        if(HostGoals.add(g)){
            this.HostGoalsTotal++;
            return true;
        }
        return false;
    }

    public boolean removeHostGoals(Goal g) {
        for(Goal goal : HostGoals){
            if(goal.getGoalHolder().equals(g.getGoalHolder())){
                goal.decAmount();
                this.HostGoalsTotal--;
                return true;
            }
        }
        if(HostGoals.remove(g)){
            this.HostGoalsTotal--;
            return true;
        }
        return false;
    }

    public List<Goal> getVisitorGoals() {
        return VisitorGoals;
    }

    public void setVisitorGoals(List<Goal> visitorGoals) {
        VisitorGoals = visitorGoals;
    }

    public boolean addVisitorGoals(Goal g) {
        for(Goal goal : VisitorGoals){
            if(goal.getGoalHolder().equals(g.getGoalHolder())){
                goal.incAmount();
                this.VisitorGoalsTotal++;
                return true;
            }
        }
        if(VisitorGoals.add(g)){
            this.VisitorGoalsTotal++;
            return true;
        }
        return false;
    }

    public boolean removeVisitorGoals(Goal g){
        for(Goal goal: VisitorGoals){
            if(goal.getGoalHolder().equals(g.getGoalHolder())){
                goal.decAmount();
                this.VisitorGoalsTotal--;
                return true;
            }
        }
        if(VisitorGoals.remove(g)){
            this.VisitorGoalsTotal--;
            return true;
        }
        return false;
    }

    public int getHostGoalsTotal() {
        return HostGoalsTotal;
    }

    public void setHostGoalsTotal(int hostGoalsTotal) {
        HostGoalsTotal = hostGoalsTotal;
    }

    public int getVisitorGoalsTotal() {
        return VisitorGoalsTotal;
    }

    public void setVisitorGoalsTotal(int visitorGoalsTotal) {
        VisitorGoalsTotal = visitorGoalsTotal;
    }
}
