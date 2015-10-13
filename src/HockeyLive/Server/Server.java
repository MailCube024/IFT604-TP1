package HockeyLive.Server;

import HockeyLive.Server.Models.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michaël on 10/12/2015.
 */
public class Server {
    private List<Game> m_runningGames;

    public Server() {
        m_runningGames = new ArrayList<>();
    }

    public static void main(String[] args){
        
    }
}
