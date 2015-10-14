package HockeyLive.Server;

import HockeyLive.Common.Communication.ServerMessage;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.Bet;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Common.Models.Penalty;
import HockeyLive.Server.Communication.ServerSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private ConcurrentMap<Integer,List<Bet>> placedBets;
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
                    ClientMessage clientMessage = socket.GetMessage();

                    Runnable handler = new HandlerThread(this, clientMessage);
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

    public void SendReply(ClientMessage clientMessage, Object data) {
        ServerMessage serverMessage = new ServerMessage(clientMessage.GetIPAddress(),
                clientMessage.GetPort(),
                clientMessage.getReceiverIp(),
                clientMessage.getReceiverPort(),
                clientMessage.getID(),
                data);
    }

    public synchronized void AddGame(Game game, GameInfo info) {
        if (runningGames.size() < 10) {
            runningGames.add(game);
            runningGameInfos.put(game, info);
            placedBets.put(game.getGameID(), new ArrayList<>());
        }
    }

    public synchronized void AddPenalty(Game game, String team, Penalty penalty) {
        GameInfo info = runningGameInfos.get(game);

        if(team.equals(game.getHost())) {
            info.getHostPenalties().add(penalty);
        } else {
            info.getVisitorPenalties().add(penalty);
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

    public synchronized Object PlaceBet(Bet bet, ClientMessage message) {
        Game game = runningGames.get(bet.getGameID());
        GameInfo info = runningGameInfos.get(game);

        if(info.getPeriod() > 2)
            return false;

        (placedBets.get(bet.getGameID())).add(bet);

        InetAddress localhost;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }

        socket.Send(new ServerMessage(localhost, Constants.SERVER_COMM_PORT,
                message.GetIPAddress(), message.GetPort(), message.getID(), true));

        while(info.getPeriod() <= 3) {
            try {
                game.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<Bet> bets = placedBets.get(game.getGameID());

        double totalAmountHost = 0;
        double totalAmountVisitor = 0;

        for (int i = 0; i < bets.size(); i++) {
            Bet b = bets.get(i);

            if(b.getBetOn().equals(game.getHost())) {
                totalAmountHost += b.getAmount();
            } else {
                totalAmountVisitor += b.getAmount();
            }
        }

        double winAmount = 0;

        if(game.getHostGoals() > game.getVisitorGoals()) {
            if(bet.getBetOn().equals(game.getHost())) {
                winAmount = 0.75 * (totalAmountHost + totalAmountVisitor)
                        * (bet.getAmount() / totalAmountHost);
            } else {
                winAmount = 0 - bet.getAmount();
            }
        } else if(game.getHostGoals() < game.getVisitorGoals()) {
            if(bet.getBetOn().equals(game.getHost())) {
                winAmount = 0.75 * (totalAmountHost + totalAmountVisitor)
                        * (bet.getAmount() / totalAmountVisitor);
            } else {
                winAmount = 0 - bet.getAmount();
            }
        }

        return winAmount;
    }

    public synchronized Object PlaceBet(Object bet, ClientMessage message) {
        try {
            Bet b = (Bet)bet;

            return PlaceBet(b, message);
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized void LeapTime(Duration duration) {
        for (int i = 0; i < runningGames.size(); ++i) {
            Game game = runningGames.get(i);
            GameInfo info = runningGameInfos.get(game);

            if(info.getPeriod() <= 3) {
                info.setPeriodChronometer(info.getPeriodChronometer().plus(duration));

                if(info.getPeriodChronometer().compareTo(Duration.ofMinutes(20)) >= 0)
                {
                    info.setPeriod(1 + info.getPeriod());
                    info.setPeriodChronometer(Duration.ofMinutes(0));
                }

                if(info.getPeriod() > 3) {
                    game.notifyAll();
                }
            }
        }
    }
}
