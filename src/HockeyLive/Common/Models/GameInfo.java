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

    public GameInfo(int id){
        this.GameID = id;
        this.PeriodChronometer = Duration.ofMinutes(20);
        this.Period = 1;
        this.HostPenalties = new ArrayList<Penalty>();
        this.VisitorPenalties = new ArrayList<Penalty>();
    }

    public int getGameID() { return this.GameID; }

    public Duration getPeriodChronometer() { return this.PeriodChronometer; }
    public void setPeriodChronometer(Duration periodChronometer) { this.PeriodChronometer = periodChronometer; }
    public void incPeriodChronometer(Duration time) { this.PeriodChronometer.plus(time); }
    public void decPeriodChronometer(Duration time) { this.PeriodChronometer.minus(time); }

    public int getPeriod() { return this.Period; }
    public void setPeriod(int period) { this.Period = period; }
    public void incPeriod() { this.Period++; }
    public void decPeriod() { this.Period--; }

    public List<Penalty> getHostPenalties() { return this.HostPenalties; }
    public void setHostPenalties(List<Penalty> hostPenalties) { this.HostPenalties = hostPenalties; }
    public boolean addHostPenalties(Penalty p) { return this.HostPenalties.add(p); }
    public boolean removeHostPenalties(Penalty p) { return this.HostPenalties.remove(p); }

    public List<Penalty> getVisitorPenalties() { return this.VisitorPenalties; }
    public void setVisitorPenalties(List<Penalty> visitorPenalties) { this.VisitorPenalties = visitorPenalties; }
    public boolean addVisitorPenalties(Penalty p) { return this.VisitorPenalties.add(p); }
    public boolean removeVisitorPenalties(Penalty p) { return this.VisitorPenalties.remove(p); }
}
