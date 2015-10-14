package HockeyLive.Server.Factory;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;

/**
 * Created by Michaël on 10/14/2015.
 */
public class GameFactory{
    private static int gameId = 0;
    private static int periodLength = 20;  // in minutes

    private static int GetNextID()
    {
        return ++gameId;
    }

    private static Game GenerateGame(String host, String visitor){
        Game newGame = new Game(GetNextID(),host,visitor);
        return newGame;
    }

    private static GameInfo GenerateGameInfo(Game game){
        GameInfo info = new GameInfo(game.getGameID(), periodLength);

        return info;
    }

    public static void UpdatePeriodLength(int minutes){
        periodLength = minutes;
    }
}
