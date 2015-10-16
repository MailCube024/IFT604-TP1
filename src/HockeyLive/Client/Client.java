package HockeyLive.Client;

import HockeyLive.Client.Communication.ClientSocket;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ClientMessageType;
import HockeyLive.Common.Communication.ServerMessage;
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
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class Client {

    private static ClientSocket socket = null;
    private static InetAddress localhost = null;

    private static ClientSocket GetSocket() {
        if (socket == null) {
            try {
                socket = new ClientSocket(Constants.CLIENT_COMM_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return socket;
    }

    private static InetAddress GetLocalhost() {
        if (localhost == null) {
            try {
                localhost = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return localhost;
    }

    public static List<Game> RequestGameList() {
        ClientMessage message = new ClientMessage(ClientMessageType.GetMatches, 1,
                GetLocalhost(), Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                null);

        GetSocket().Send(message);

        try {
            ServerMessage serverMessage = GetSocket().GetMessage();
            List<Game> gameList = (List<Game>)serverMessage.getData();

            return gameList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<Game>();
    }

    public static GameInfo RequestGameInfo(Integer gameID) {
        ClientMessage message = new ClientMessage(ClientMessageType.GetMatchInfo, 2,
                GetLocalhost(), Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                gameID);

        GetSocket().Send(message);

        try {
            ServerMessage serverMessage = GetSocket().GetMessage();
            GameInfo gameInfo = (GameInfo)serverMessage.getData();

            return gameInfo;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean SendBet(Bet bet) {
        ClientMessage message = new ClientMessage(ClientMessageType.PlaceBet, 3,
                GetLocalhost(), Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                bet);

        GetSocket().Send(message);

        try {
            ServerMessage serverMessage = GetSocket().GetMessage();
            boolean betRegistered = (boolean)serverMessage.getData();

            return betRegistered;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
