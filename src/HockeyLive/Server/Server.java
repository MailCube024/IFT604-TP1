package HockeyLive.Server;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Michaël on 10/12/2015.
 */
public class Server {
    private ConcurrentMap<Integer, Game> runningGames;
    private ConcurrentMap<Game,GameInfo> runningGameInfos;

    public Server() {
        runningGames = new ConcurrentHashMap<>();
        runningGameInfos = new ConcurrentHashMap<>();
    }

    public static void main(String[] args){
        
    }
}
