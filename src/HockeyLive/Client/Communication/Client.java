package HockeyLive.Client.Communication;

import HockeyLive.Client.Listeners.BetConfirmationListener;
import HockeyLive.Client.Listeners.BetUpdateListener;
import HockeyLive.Client.Listeners.GameInfoUpdateListener;
import HockeyLive.Client.Listeners.GameListUpdateListener;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ClientMessageType;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.Bet;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Micha�l Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Client {

    //TODO: Create a thread that takes messages from socket and sends it to UI Handling (see server handler thread)
    //TODO: Create Task and Scheduler for refresh. Stop and Restart when pushing refresh button
    /*
    //get reference to the future
    Future<?> future = service.scheduleAtFixedRate(runnable, INITIAL_DELAY, INTERVAL, TimeUnit.SECONDS)
    //cancel instead of shutdown
    future.cancel(true);
    //schedule again (reuse)
    future = service.scheduleAtFixedRate(runnable, INITIAL_DELAY, INTERVAL, TimeUnit.SECONDS)
    //shutdown when you don't need to reuse the service anymore
    service.shutdown()
     */
    private List<GameInfoUpdateListener> gameInfoUpdateListeners;
    private List<GameListUpdateListener> gameListUpdateListeners;
    private List<BetConfirmationListener> betConfirmationListeners;
    private List<BetUpdateListener> betUpdateListeners;

    private ClientSocket socket = null;
    private InetAddress localhost = null;

    private Thread receiver;

    public Client() {
        gameInfoUpdateListeners = new ArrayList<>();
        gameListUpdateListeners = new ArrayList<>();
        betConfirmationListeners = new ArrayList<>();
        betUpdateListeners = new ArrayList<>();
    }

    private ClientSocket GetSocket() {
        if (socket == null) {
            try {
                socket = new ClientSocket(Constants.CLIENT_COMM_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return socket;
    }

    private InetAddress GetLocalhost() {
        if (localhost == null) {
            try {
                localhost = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return localhost;
    }

    public void RequestGameList() {
        ClientMessage message = new ClientMessage(ClientMessageType.GetMatches, 1,
                GetLocalhost(), Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                null);

        GetSocket().Send(message);
    }

    public void RequestGameInfo(Integer gameID) {
        ClientMessage message = new ClientMessage(ClientMessageType.GetMatchInfo, 2,
                GetLocalhost(), Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                gameID);

        GetSocket().Send(message);
    }

    public void SendBet(Bet bet) {
        ClientMessage message = new ClientMessage(ClientMessageType.PlaceBet, 3,
                GetLocalhost(), Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                bet);

        GetSocket().Send(message);
    }

    public void ReceiveBet(Bet bet) {
        for (BetUpdateListener l : betUpdateListeners)
            l.BetUpdate(bet);

        ClientMessage message = new ClientMessage(ClientMessageType.AckNotification, 4,
                GetLocalhost(), Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                bet);

        GetSocket().Send(message);
    }

    public void ReceiveGameList(List<Game> games) {
        for (GameListUpdateListener l : gameListUpdateListeners)
            l.UpdateGameList(games);
    }

    public void ReceiveGameInfo(GameInfo info) {
        for (GameInfoUpdateListener l : gameInfoUpdateListeners)
            l.UpdateGameInfo(info);
    }

    public void ReceiveBetConfirmation(boolean confirmation) {
        for (BetConfirmationListener l : betConfirmationListeners)
            l.IsBetConfirmed(confirmation);
    }

    public void AddGameListUpdateListener(GameListUpdateListener listener) {
        gameListUpdateListeners.add(listener);
    }

    public void AddGameInfoUpdateListener(GameInfoUpdateListener listener) {
        gameInfoUpdateListeners.add(listener);
    }

    public void AddBetConfirmationListener(BetConfirmationListener listener) {
        betConfirmationListeners.add(listener);
    }

    public void AddBetUpdateListener(BetUpdateListener listener) {
        betUpdateListeners.add(listener);
    }

    public void UnregisterListeners() {
        gameInfoUpdateListeners.clear();
        gameListUpdateListeners.clear();
        betConfirmationListeners.clear();
        betUpdateListeners.clear();
    }

    public void Start() {
        receiver = new Thread(new MessageHandler(this, GetSocket()));
        receiver.start();
    }

    public void Close() {
        receiver.interrupt();
        socket.CloseSocket();
    }
}
