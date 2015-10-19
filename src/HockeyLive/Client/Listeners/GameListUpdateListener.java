package HockeyLive.Client.Listeners;

import HockeyLive.Common.Models.Game;

import java.util.List;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public interface GameListUpdateListener {
    void UpdateGameList(List<Game> games);
}
