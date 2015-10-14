package HockeyLive.Common.Models;

import java.io.Serializable;
import java.util.Random;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Game implements Serializable {
    private int GameID;
    private String Host;
    private String Visitor;

    public Game(int id, String host, String visitor) {
        this.GameID = id;
        this.Host = host;
        this.Visitor = visitor;
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

    public String toString(){
        return String.format("%s vs. %s",Host,Visitor);
    }
}
