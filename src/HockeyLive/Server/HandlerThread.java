package HockeyLive.Server;

import HockeyLive.Common.Communication.Request;

/**
 * Created by Benoit on 2015-10-13.
 */
public class HandlerThread implements Runnable {

    private Server server;
    private Request request;

    public HandlerThread(Server server, Request request){
        this.server = server;
        this.request = request;
    }

    @Override
    public void run() {
        Object replyData = null;

        switch (request.getType()) {
            case GetMatches:
                replyData = server.GetMatches();
                break;
            case GetMatchInfo:
                replyData = server.GetMatchInfo(request.getRequestData());
                break;
            case PlaceBet:
                break;
            default:
                break;
        }

        server.SendReply(request, replyData);
    }
}