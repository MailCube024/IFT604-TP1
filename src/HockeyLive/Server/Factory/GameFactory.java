package HockeyLive.Server.Factory;

/**
 * Created by Michaël on 10/14/2015.
 */
public class GameFactory{
    private static int GAME_ID = 0;

    private static int GenerateID()
    {
        return ++GAME_ID;
    }
}
