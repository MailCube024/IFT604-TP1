package HockeyLive.Common.Models;

import java.io.Serializable;
import java.time.Duration;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Penalty implements Serializable {
    public static final int LONG_PENALTY = 5;   // in minutes
    public static final int SHORT_PENALTY = 2;  // in minutes

    private String PenaltyHolder;
    private Duration TimeLeft;

    public Penalty(String holder, Duration time) {
        this.PenaltyHolder = holder;
        this.TimeLeft = time;
    }

    public String getPenaltyHolder() {
        return PenaltyHolder;
    }

    public void setPenaltyHolder(String penaltyHolder) {
        PenaltyHolder = penaltyHolder;
    }

    public Duration getTimeLeft() {
        return TimeLeft;
    }

    public void setTimeLeft(Duration timeLeft) {
        TimeLeft = timeLeft;
    }

    public void incTimeLeft(Duration time) {
        TimeLeft = TimeLeft.plus(time);
    }

    public void decTimeLeft(Duration time) {
        TimeLeft = TimeLeft.minus(time);
    }

    public String toString() {
        return String.format("%s, %d:%02d", PenaltyHolder, TimeLeft.getSeconds() / 60, TimeLeft.getSeconds() % 60);
    }
}
