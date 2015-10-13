package HockeyLive.Server.Models;

import java.time.Duration;

/**
 * Created by Michaël on 10/12/2015.
 */
public class Game {
    private String host;
    private String visitor;
    private int hostGoals;
    private int visitorGoals;
    private Duration periodChronometer;
    private int period;

    private String penaltyTo;
    private Duration penaltyTimeLeft;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    public int getHostGoals() {
        return hostGoals;
    }

    public void setHostGoals(int hostGoals) {
        this.hostGoals = hostGoals;
    }

    public int getVisitorGoals() {
        return visitorGoals;
    }

    public void setVisitorGoals(int visitorGoals) {
        this.visitorGoals = visitorGoals;
    }

    public Duration getPeriodChronometer() {
        return periodChronometer;
    }

    public void setPeriodChronometer(Duration periodChronometer) {
        this.periodChronometer = periodChronometer;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getPenaltyTo() {
        return penaltyTo;
    }

    public void setPenaltyTo(String penaltyTo) {
        this.penaltyTo = penaltyTo;
    }

    public Duration getPenaltyTimeLeft() {
        return penaltyTimeLeft;
    }

    public void setPenaltyTimeLeft(Duration penaltyTimeLeft) {
        this.penaltyTimeLeft = penaltyTimeLeft;
    }
}
