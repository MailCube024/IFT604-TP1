import HockeyLive.Client.Communication.ClientSocket;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ClientMessageType;
import HockeyLive.Common.Communication.ServerMessage;
import HockeyLive.Common.Communication.ServerMessageType;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Common.helpers.SerializationHelper;
import HockeyLive.Server.Communication.ServerSocket;
import HockeyLive.Server.Factory.GameFactory;
import HockeyLive.Server.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) return;

        if (args[0].equalsIgnoreCase("-s")) {
            //TODO: Start server
        } else if (args[0].equalsIgnoreCase("-c")) {
            //TODO: Start client
        } else if (args[0].equalsIgnoreCase("-t")) {
            //TestSerializationHelper();
            //TestClientAndServerSocket();
            //TestServerGameCreation();
            //TestServerRunningState();
            //TestServerChronometerUpdate();
            //TestServerChronometerChangePeriod();
            TestServerChronometerCompleteGame();
        } else {
            System.out.println("Command not recognized - Stopping application");
        }
    }

    private static void TestServerChronometerCompleteGame() {
        GameFactory.UpdatePeriodLength(1);
        Server server = new Server();

        List<Game> games = server.GetGames();
        try {
            server.start();
            try {
                //Waiting ticks to be completed by the timers (~8 ticks)
                Thread.sleep(240 * 1000);
            } catch (InterruptedException e) {
            }
            List<Game> nonCompletedGames = server.GetNonCompletedGames();
            assert games.size() != nonCompletedGames.size();
            System.out.println("Chronometer Completing Games OK");
        } finally {
            server.stop();
        }
        GameFactory.UpdatePeriodLength(20);
    }

    private static void TestServerChronometerChangePeriod() {
        GameFactory.UpdatePeriodLength(1);
        Server server = new Server();

        Game game = server.GetGames().get(0);
        GameInfo startInfo = server.GetGameInfo(game.getGameID());
        try {
            server.start();
            try {
                //Waiting ticks to be completed by the timers (~4 ticks)
                Thread.sleep(120 * 1000);
            } catch (InterruptedException e) {
            }
            GameInfo alteredInfo = server.GetGameInfo(game.getGameID());
            assert startInfo.getPeriod() != alteredInfo.getPeriod();
            System.out.println("Chronometer period update OK");
        } finally {
            server.stop();
        }
        GameFactory.UpdatePeriodLength(20);
    }

    private static void TestServerChronometerUpdate() {
        Server server = new Server();
        Game game = server.GetGames().get(0);
        GameInfo startInfo = server.GetGameInfo(game.getGameID());
        try {
            server.start();
            try {
                //Waiting ticks to be completed by the timers (2 ticks)
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
            }
            GameInfo alteredInfo = server.GetGameInfo(game.getGameID());
            assert !startInfo.getPeriodChronometer().equals(alteredInfo.getPeriodChronometer());
            System.out.println("Chronometer update OK");
        } finally {
            server.stop();
        }
    }

    private static void TestServerRunningState() {
        Server server = new Server();
        try {
            server.start();
            System.out.println("Server start OK");
        } finally {
            server.stop();
            System.out.println("Server stop OK");
        }

    }

    private static void TestServerGameCreation() {
        Server server = new Server();
        List<Game> games = server.GetGames();
        assert games.size() == 10;

        games = server.GetNonCompletedGames();
        assert games.size() == 10;

        List<GameInfo> infos = server.GetGameInfos();
        assert infos.size() == 10;
        final List<Game> finalGames = games;
        assert infos.stream().filter(i -> i.getGameID() == finalGames.get(0).getGameID()).count() != 0;

        System.out.println("Game creation OK");
    }

    // Testing Server Socket & Client Socket
    private static void TestClientAndServerSocket() {

        ClientSocket client = null;
        ServerSocket server = null;
        try {
            client = new ClientSocket(Constants.CLIENT_COMM_PORT);
            server = new ServerSocket(Constants.SERVER_COMM_PORT);

            ClientMessage req = new ClientMessage(ClientMessageType.GetMatches, 2, InetAddress.getLocalHost(),
                    Constants.SERVER_COMM_PORT, InetAddress.getLocalHost(), Constants.CLIENT_COMM_PORT, null);
            client.Send(req);
            ClientMessage clientClientMessage = server.GetMessage();

            if (clientClientMessage.getID() == req.getID()) {
                System.out.println("Client socket send request OK");
                ServerMessage rep = new ServerMessage(ServerMessageType.ReturnGames, clientClientMessage.GetIPAddress(), clientClientMessage.GetPort(),
                        clientClientMessage.getReceiverIp(), clientClientMessage.getReceiverPort(),
                        clientClientMessage.getID(), new Game(1, "Host", "Visitor"));
                server.Send(rep);
                ServerMessage serverServerMessage = client.GetMessage();
                if (serverServerMessage.getRequestID() == req.getID()) {
                    System.out.println("Server socket send reply OK");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (client != null)
                client.CloseSocket();
            if (server != null)
                server.CloseSocket();
        }
    }

    //Testing marshalling
    private static void TestSerializationHelper() {
        ClientMessage r = null;
        try {
            r = new ClientMessage(ClientMessageType.GetMatches, 2, InetAddress.getLocalHost(),
                    Constants.SERVER_COMM_PORT, InetAddress.getLocalHost(), Constants.CLIENT_COMM_PORT, null);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        byte[] arr;
        try {
            arr = SerializationHelper.serialize(r);
            ClientMessage re = (ClientMessage) SerializationHelper.deserialize(arr);
            if (re.getID() == r.getID()) System.out.println("Marshalling OK");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
