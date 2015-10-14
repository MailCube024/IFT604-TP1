package HockeyLive.Common.Models;

import java.io.Serializable;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Bet implements Serializable {
    private double amount;
    private String betOn;

    public Bet(double amount, String team){
        this.amount = amount;
        this.betOn = team;
    }

    public double getAmount() { return this.amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getBetOn() { return this.betOn; }
    public void setBetOn(String betOn) { this.betOn = betOn; }
}
