package HockeyLive.Client.Communication;

import HockeyLive.Common.Communication.ServerMessage;
import HockeyLive.Common.Communication.ServerMessageType;
import HockeyLive.Common.Models.Bet;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;

import java.util.List;

/**
 * Created by Michaël on 10/16/2015.
 */
public class MessageHandler implements Runnable {

    private final Client client;
    private final ClientSocket socket;

    public MessageHandler(Client client, ClientSocket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ServerMessage message = socket.GetMessage();
                new Thread(() -> InformClient(message.getType(), message.getData())).start();

            } catch (InterruptedException e) {
                System.out.println("MessageHandler: Interrupted while getting message - Stopping thread");
                //e.printStackTrace();
                break;
            }
        }
    }

    public void InformClient(ServerMessageType type, Object data) {
        switch (type) {
            case ReturnGameInfo:
                client.ReceiveGameInfo((GameInfo) data);
                break;
            case ReturnGames:
                client.ReceiveGameList((List<Game>) data);
                break;
            case BetConfirmation:
                client.ReceiveBetConfirmation((boolean) data);
                break;
            case BetResult:
                client.ReceiveBet((Bet) data);
                break;
        }
    }
}
