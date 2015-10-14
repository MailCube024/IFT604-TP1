package HockeyLive.Server;

import HockeyLive.Common.Communication.ClientMessage;

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
        Object replyData = null;

        switch (clientMessage.getType()) {
            case GetMatches:
                replyData = server.GetMatches();
                break;
            case GetMatchInfo:
                replyData = server.GetMatchInfo(clientMessage.getData());
                break;
            case PlaceBet:
                break;
            default:
                break;
        }

        server.SendReply(clientMessage, replyData);
    }
}