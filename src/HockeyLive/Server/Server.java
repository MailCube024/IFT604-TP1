package HockeyLive.Server;

import HockeyLive.Common.Communication.Reply;
import HockeyLive.Common.Communication.Request;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.Bet;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Server.Communication.ServerSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Michaël on 10/12/2015.
 */
public class Server {
    private List<Game> runningGames;
    private ConcurrentMap<Game,GameInfo> runningGameInfos;
    private ConcurrentMap<Game,List<Bet>> placedBets;
    private ServerSocket socket;

    public Server() {
        runningGames = new ArrayList<>();
        runningGameInfos = new ConcurrentHashMap<>();
        placedBets = new ConcurrentHashMap<>();
    }

    public void execute() {
        try {
            socket = new ServerSocket(Constants.SERVER_COMM_PORT);
            Executor threadPool = Executors.newFixedThreadPool(50);

            while (true)
            {
                socket.Receive();

                try {
                    Request request = socket.GetRequest();

                    Runnable handler = new HandlerThread(this, request);
                    threadPool.execute(handler);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Server server = new Server();
        server.execute();
    }

    public void SendReply(Request request, Object data) {
        Reply reply = new Reply(request.GetIPAddress(),
                request.GetPort(),
                request.getReceiverIp(),
                request.getReceiverPort(),
                request.getID(),
                data);
    }

    public synchronized void AddGame(Game game, GameInfo info) {
        if (runningGames.size() < 10) {
            runningGames.add(game);
            runningGameInfos.put(game, info);
            placedBets.put(game, new ArrayList<>());
        }
    }

    public synchronized List<Game> GetMatches() {
        return runningGames;
    }

    public synchronized GameInfo GetMatchInfo(Game match) {
        return runningGameInfos.get(match);
    }

    public synchronized GameInfo GetMatchInfo(Object match) {
        try {
            Game m = (Game)match;
            return GetMatchInfo(m);
        } catch (Exception e) {
            return  null;
        }
    }

    public synchronized boolean PlaceBet(Bet bet) {
        (placedBets.get(bet.getGame())).add(bet);
        return true;
    }

    public synchronized boolean PlaceBet(Object bet) {
        try {
            Bet b = (Bet)bet;
            return PlaceBet(b);
        } catch (Exception e) {
            return false;
        }
    }

    public void SendResults(Game game) {

    }
}
