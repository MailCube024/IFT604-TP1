package HockeyLive.Common.Models;

import java.io.Serializable;

/**
 * Created by Bruno-Pier on 2015-10-14.
 */
public class Goal implements Serializable {
    private String GoalHolder;
    private int amount;

    public Goal(String player) {
        this.GoalHolder = player;
        amount = 1;
    }

    public String getGoalHolder() {
        return GoalHolder;
    }

    public void setGoalHolder(String goalHolder) {
        GoalHolder = goalHolder;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void incAmount() {
        this.amount++;
    }

    public void decAmount() {
        this.amount--;
    }

    public String toString() {
        return String.format("%s,   %d", GoalHolder, amount);
    }
}
