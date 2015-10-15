package HockeyLive.Client;

import HockeyLive.Client.Communication.ClientSocket;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ClientMessageType;
import HockeyLive.Common.Communication.ServerMessage;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michaël on 10/12/2015.
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

    public static ArrayList<Game> RequestGameList() {
        ClientMessage message = new ClientMessage(ClientMessageType.GetMatches, 1,
                GetLocalhost(), Constants.SERVER_COMM_PORT,
                GetLocalhost(), Constants.CLIENT_COMM_PORT,
                null);

        GetSocket().Send(message);

        try {
            ServerMessage serverMessage = GetSocket().GetMessage();
            ArrayList<Game> gameList = (ArrayList<Game>)serverMessage.getData();

            return gameList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<Game>();
    }
}
