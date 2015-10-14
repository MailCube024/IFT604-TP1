package HockeyLive.Common.Models;

import java.io.Serializable;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Game implements Serializable {
    private int GameID;
    private String Host;
    private String Visitor;
    private int HostGoals;
    private int VisitorGoals;
    private boolean Completed;

    public Game(int id, String host, String visitor) {
        GameID = id;
        Host = host;
        Visitor = visitor;
        HostGoals = 0;
        VisitorGoals = 0;
        Completed = false;
    }

    public int getGameID() {
        return GameID;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getVisitor() {
        return Visitor;
    }

    public void setVisitor(String visitor) {
        Visitor = visitor;
    }

    public int getHostGoals() {
        return HostGoals;
    }

    public void setHostGoals(int hostGoals) {
        HostGoals = hostGoals;
    }

    public void incHostGoals() {
        HostGoals++;
    }

    public void decHostGoals() {
        HostGoals--;
    }

    public int getVisitorGoals() {
        return VisitorGoals;
    }

    public void setVisitorGoals(int visitorGoals) {
        VisitorGoals = visitorGoals;
    }

    public void incVisitorGoals() {
        VisitorGoals++;
    }

    public void decVisitorGoals() {
        VisitorGoals--;
    }

    public String GetGameDescription() {
        return String.format("%s vs. %s / %d-%d", Host, Visitor, HostGoals, VisitorGoals);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Game) {
            return ((Game) obj).getGameID() == GameID;
        } else
            return super.equals(obj);
    }

    public boolean isCompleted() {
        return Completed;
    }

    public void setCompleted(boolean completed) {
        Completed = completed;
    }
}
