package HockeyLive.Common.Models;

import java.io.Serializable;
import java.time.Duration;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Penalty implements Serializable {
    private String PenaltyHolder;
    private Duration TimeLeft;

    public Penalty(String holder, Duration time) {
        this.PenaltyHolder = holder;
        this.TimeLeft = time;
    }

    public String getPenaltyHolder() { return this.PenaltyHolder; }
    public void setPenaltyHolder(String penaltyHolder) { this.PenaltyHolder = penaltyHolder; }

    public Duration getTimeLeft() { return this.TimeLeft; }
    public void setTimeLeft(Duration timeLeft) { this.TimeLeft = timeLeft; }
    public void incTimeLeft(Duration time) { this.TimeLeft.plus(time); }
    public void decTimeLeft(Duration time) { this.TimeLeft.minus(time); }
}
