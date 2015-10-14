package HockeyLive.Server;

import HockeyLive.Common.Communication.Notification;
import HockeyLive.Common.Communication.Reply;
import HockeyLive.Common.Communication.Request;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.Bet;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Common.Models.Penalty;
import HockeyLive.Server.Communication.ServerSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
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
    private ConcurrentMap<Integer,List<BetInfo>> placedBets;
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

            //threadPool.execute(cmdHandler);

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
            placedBets.put(game.GameID, new ArrayList<>());
        }
    }

    public synchronized void AddPenalty(Game game, Penalty penalty) {
        GameInfo info = runningGameInfos.get(game);
        info.Penalties.add(penalty);
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

    public synchronized boolean PlaceBet(BetInfo bet) {
        (placedBets.get(bet.getBet().getGameID())).add(bet);
        return true;
    }

    public synchronized boolean PlaceBet(Object bet, InetAddress betterIp, int betterPort) {
        try {
            Bet b = (Bet)bet;

            BetInfo info = new BetInfo();
            info.setBet(b);
            info.setBetterIp(betterIp);
            info.setBetterPort(betterPort);

            return PlaceBet(info);
        } catch (Exception e) {
            return false;
        }
    }

    public void SendResults(Game game) {
        List<BetInfo> bets = placedBets.get(game.GameID);

        double totalAmountHost = 0;
        double totalAmountVisitor = 0;

        for (int i = 0; i < bets.size(); i++) {
            Bet bet = bets.get(i).getBet();

            if(bet.getBetOn().equals(game.Host)) {
                totalAmountHost += bet.getAmount();
            } else {
                totalAmountVisitor += bet.getAmount();
            }
        }

        if(game.HostGoals > game.VisitorGoals) {
            for (int i = 0; i < bets.size(); i++) {
                BetInfo info = bets.get(i);
                if(info.getBet().getBetOn().equals(game.Host)) {
                    double winAmount = 0.75 * (totalAmountHost + totalAmountVisitor)
                            * (info.getBet().getAmount() / totalAmountHost);
                    new Notification(InetAddress.getLocalHost(), Constants.SERVER_COMM_PORT,
                            info.getBetterIp(), info.getBetterPort(),
                            1, winAmount);
                } else {
                    socket.
                    new Notification(InetAddress.getLocalHost(), Constants.SERVER_COMM_PORT,
                            info.getBetterIp(), info.getBetterPort(),
                            1, 0 - info.getBet().getAmount());
                }
            }
        } else if(game.HostGoals < game.VisitorGoals) {
            for (int i = 0; i < bets.size(); i++) {
                BetInfo info = bets.get(i);
                if(info.getBet().getBetOn().equals(game.Visitor)) {
                    double winAmount = 0.75 * (totalAmountHost + totalAmountVisitor)
                            * (info.getBet().getAmount() / totalAmountVisitor);
                    new Notification(InetAddress.getLocalHost(), Constants.SERVER_COMM_PORT,
                            info.getBetterIp(), info.getBetterPort(),
                            1, winAmount);
                } else {
                    new Notification(InetAddress.getLocalHost(), Constants.SERVER_COMM_PORT,
                            info.getBetterIp(), info.getBetterPort(),
                            1, 0 - info.getBet().getAmount());
                }
            }
        } else {
            for (int i = 0; i < bets.size(); i++) {
                BetInfo info = bets.get(i);
                new Notification(InetAddress.getLocalHost(), Constants.SERVER_COMM_PORT,
                        info.getBetterIp(), info.getBetterPort(),
                        1, 0);
            }
        }
    }

    public synchronized void LeapTime(Duration duration) {
        for (int i = 0; i < runningGames.size(); ++i) {
            Game game = runningGames.get(i);
            GameInfo info = runningGameInfos.get(game);

            if(info.Period <= 3) {
                info.PeriodChronometer = info.PeriodChronometer.plus(duration);

                if(info.PeriodChronometer.compareTo(Duration.ofMinutes(20)) >= 0)
                {
                    ++info.Period;
                    info.PeriodChronometer = Duration.ofMinutes(0);
                }

                if(info.Period > 3) {
                    SendResults(game);
                }
            }
        }
    }
}
