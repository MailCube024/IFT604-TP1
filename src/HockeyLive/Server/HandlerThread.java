package HockeyLive.Server;

import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ServerMessageType;

/**
 * Created by Benoit on 2015-10-13.
 */
public class HandlerThread implements Runnable {

    private Server server;
    private ClientMessage clientMessage;

    public HandlerThread(Server server, ClientMessage clientMessage){
        this.server = server;
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        Object replyData;

        switch (clientMessage.getType()) {
            case GetMatches:
                replyData = server.GetGames();
                server.SendReply(ServerMessageType.ReturnGames, clientMessage, replyData);
                break;
            case GetMatchInfo:
                replyData = server.GetGameInfo(clientMessage.getData());
                server.SendReply(ServerMessageType.ReturnGameInfo, clientMessage, replyData);
                break;
            case PlaceBet:
                server.PlaceBet(clientMessage.getData(), clientMessage);
                break;
            case AckNotification:
                server.AddAck(clientMessage);
                break;
            default:
                break;
        }

    }
}