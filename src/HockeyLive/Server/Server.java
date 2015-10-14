package HockeyLive.Server;

import HockeyLive.Common.Communication.Reply;
import HockeyLive.Common.Communication.Request;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;
import HockeyLive.Server.Communication.UDPServerSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Michaël on 10/12/2015.
 */
public class Server {
    private ConcurrentMap<Integer, Game> runningGames;
    private ConcurrentMap<Game,GameInfo> runningGameInfos;

    public Server() {
        runningGames = new ConcurrentHashMap<>();
        runningGameInfos = new ConcurrentHashMap<>();
    }

    public static void main(String[] args){

        try {
            UDPServerSocket socketUDP = new UDPServerSocket(4444);
            Executor threadPool = Executors.newFixedThreadPool(50);

            while (true)
            {
                socketUDP.Receive();

                try {
                    Request request = socketUDP.GetRequest();

                    Runnable handler = new HandlerThread(socketUDP, request);
                    threadPool.execute(handler);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
