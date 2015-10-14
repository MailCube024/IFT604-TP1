package HockeyLive.Common.Models;

import java.io.Serializable;

/**
 * Created by Michaël on 10/13/2015.
 */
public class Game implements Serializable{
    public int GameID;
    public String Host;
    public String Visitor;
    public int HostGoals;
    public int VisitorGoals;

    public String GetGameDescription(){
        return String.format("%s vs. %s / %d-%d",Host,Visitor,HostGoals,VisitorGoals);
    }

    public Game(){}
}
