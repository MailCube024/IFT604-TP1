package HockeyLive.Common.Models;

import java.io.Serializable;
import java.util.Random;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Game implements Serializable{
    private int GameID;
    private String Host;
    private String Visitor;
    private int HostGoals;
    private int VisitorGoals;

    public Game(int id, String host, String visitor){
        this.GameID = id;
        this.Host = host;
        this.Visitor = visitor;
        this.HostGoals = 0;
        this.VisitorGoals = 0;
    }

    public int getGameID() { return this.GameID; }

    public String getHost() { return this.Host; }
    public void setHost(String host) { this.Host = host; }

    public String getVisitor() { return this.Visitor; }
    public void setVisitor(String visitor) { this.Visitor = visitor; }

    public int getHostGoals() { return this.HostGoals; }
    public void setHostGoals(int hostGoals) { this.HostGoals = hostGoals; }
    public void incHostGoals() { this.HostGoals++; }
    public void decHostGoals() { this.HostGoals--; }

    public int getVisitorGoals() { return this.VisitorGoals; }
    public void setVisitorGoals(int visitorGoals) { this.VisitorGoals = visitorGoals; }
    public void incVisitorGoals() { this.VisitorGoals++; }
    public void decVisitorGoals() { this.VisitorGoals--; }

    public String toString(){
        return String.format("%s vs. %s / %d-%d",Host,Visitor,HostGoals,VisitorGoals);
    }
}
