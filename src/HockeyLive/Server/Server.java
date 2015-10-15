package HockeyLive.Server;

import HockeyLive.Common.Communication.ServerMessage;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.*;
import HockeyLive.Server.Communication.ServerSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Micha�l on 10/12/2015.
 */
public class Server implements Runnable {
    private ArrayList<Game> runningGames;
    private ConcurrentMap<Game,GameInfo> runningGameInfos;
    private ConcurrentMap<Integer,List<Bet>> placedBets;
    private ConcurrentMap<Integer,ConcurrentMap<InetAddress,ConcurrentMap<Integer,ClientMessage>>> acks;
    private ServerSocket socket;
    private Thread serverThread;

    public Server() {
        runningGames = new ArrayList<>();
        runningGameInfos = new ConcurrentHashMap<>();
        placedBets = new ConcurrentHashMap<>();
        acks = new ConcurrentHashMap<>();

        /**For test purpose creating a List with stuff in it. REMOVE AFTER TEST IS DONE.**/
        GameInfo info1 = new GameInfo(1, 20);
        AddGame(new Game(1, "Montreal", "Ottawa"), info1);
        info1.addHostGoals(new Goal("Montreal player #56"));
        info1.addHostGoals(new Goal("Montreal player #56"));
        info1.addHostGoals(new Goal("Montreal player #32"));
        info1.addVisitorGoals(new Goal("Ottawa player #87"));
        info1.addHostPenalties(new Penalty("Montreal player #45", Duration.ofMinutes(2)));
        info1.addVisitorPenalties(new Penalty("Ottawa player #22", Duration.ofMinutes(10)));
        info1.addVisitorPenalties(new Penalty("Ottawa player #53", Duration.ofMinutes(2)));

        AddGame(new Game(2, "Vancouver", "Calgary"), new GameInfo(2, 10));
        AddGame(new Game(3, "San-Jose", "St-Louis"), new GameInfo(3, 10));

        /****************************************************************************/

    }

    public void execute() {
        try {
            socket = new ServerSocket(Constants.SERVER_COMM_PORT);
            ExecutorService threadPool = Executors.newCachedThreadPool();

            //threadPool.execute(cmdHandler);

            while (true)
            {
                //socket.Receive();

                try {
                    ClientMessage clientMessage = socket.GetMessage();

                    Runnable handler = new HandlerThread(this, clientMessage);
                    threadPool.submit(handler);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendReply(ClientMessage clientMessage, Object data) {
        ServerMessage serverMessage = new ServerMessage(clientMessage.GetIPAddress(),
                clientMessage.GetPort(),
                clientMessage.getReceiverIp(),
                clientMessage.getReceiverPort(),
                clientMessage.getID(),
                data);

        socket.Send(serverMessage);
    }

    public synchronized void AddGame(Game game, GameInfo info) {
        if (runningGames.size() < 10) {
            runningGames.add(game);
            runningGameInfos.put(game, info);
            placedBets.put(game.getGameID(), new ArrayList<>());
            acks.put(game.getGameID(), new ConcurrentHashMap<>());
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

    public synchronized ArrayList<Game> GetMatches() {
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

    public void AddAck(ClientMessage message) {
        Bet bet = (Bet)message.getData();

        ConcurrentMap<InetAddress,ConcurrentMap<Integer,ClientMessage>> gameAcks = acks.get(message.getID());
        gameAcks.putIfAbsent(message.GetIPAddress(), new ConcurrentHashMap<Integer,ClientMessage>());
        ConcurrentMap<Integer,ClientMessage> addressAcks = gameAcks.get(message.GetIPAddress());
        addressAcks.put(message.GetPort(), message);

        acks.notifyAll();
    }

    public synchronized void PlaceBet(Bet bet, ClientMessage message) {
        Game game = runningGames.get(bet.getGameID());
        GameInfo info = runningGameInfos.get(game);

        boolean added = false;

        InetAddress localhost;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        if(info.getPeriod() <= 2) {
            added = true;
            (placedBets.get(bet.getGameID())).add(bet);
        }

        socket.Send(new ServerMessage(localhost, Constants.SERVER_COMM_PORT,
                message.GetIPAddress(), message.GetPort(), message.getID(), added));

        while(info.getPeriod() <= 3) {
            try {
                game.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        bet.setAmountGained(computeAmountGained(bet, game, info));

        ServerMessage serverMessage = new ServerMessage(message.GetIPAddress(),
                message.GetPort(),
                message.getReceiverIp(),
                message.getReceiverPort(),
                message.getID(),
                bet);

        while (! (acks.containsKey(game.getGameID())
                && acks.get(game.getGameID()).containsKey(message.GetIPAddress())
                && acks.get(game.getGameID()).get(message.GetIPAddress()).containsKey(message.GetPort()))) {
            socket.Send(serverMessage);
            try {
                acks.wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void PlaceBet(Object bet, ClientMessage message) {
        try {
            Bet b = (Bet) bet;
            PlaceBet(b, message);
        } catch (Exception e) {
            SendReply(message, false);
        }
    }
    
    private double computeAmountGained(Bet bet, Game game, GameInfo info) {
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

        double amountGained = 0;

        if(info.getHostGoalsTotal() > info.getVisitorGoalsTotal()) {
            if(bet.getBetOn().equals(game.getHost())) {
                amountGained = 0.75 * (totalAmountHost + totalAmountVisitor)
                        * (bet.getAmount() / totalAmountHost);
            } else {
                amountGained = 0 - bet.getAmount();
            }
        } else if(info.getHostGoalsTotal() < info.getVisitorGoalsTotal()) {
            if(bet.getBetOn().equals(game.getHost())) {
                amountGained = 0.75 * (totalAmountHost + totalAmountVisitor)
                        * (bet.getAmount() / totalAmountVisitor);
            } else {
                amountGained = 0 - bet.getAmount();
            }
        }
        
        return amountGained;
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

    @Override
    public void run() {
        execute();
    }

    public void start(){
        serverThread = new Thread(this);
        serverThread.start();
    }

    public static void main(String[] args){
        Server server = new Server();
        server.start();
    }
}
