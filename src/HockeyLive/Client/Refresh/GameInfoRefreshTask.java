package HockeyLive.Client.Refresh;

import HockeyLive.Client.Communication.Client;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class GameInfoRefreshTask implements Runnable {
    private final Client client;
    private int selectedGameID = 0;

    public GameInfoRefreshTask(Client client, int selectedGameID) {
        this.client = client;
        this.selectedGameID = selectedGameID;
    }

    @Override
    public void run() {
        System.out.println("Requesting automatic refresh");
        if (selectedGameID == 0) {
            System.out.println("No currently selected game");
            return;
        }
        client.RequestGameInfo(selectedGameID);
        System.out.println("Request sent for game with ID " + selectedGameID);
    }
}
