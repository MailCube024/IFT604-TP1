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
    private int periodLength;

    public GameInfo(int id, int periodLength) {
        this.GameID = id;
        this.PeriodChronometer = Duration.ofMinutes(periodLength);
        this.periodLength = periodLength;
        this.Period = 1;
        this.HostPenalties = new ArrayList<>();
        this.VisitorPenalties = new ArrayList<>();
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
}
