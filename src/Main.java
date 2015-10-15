import HockeyLive.Client.Communication.ClientSocket;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ClientMessageType;
import HockeyLive.Common.Communication.ServerMessage;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.helpers.SerializationHelper;
import HockeyLive.Server.Communication.ServerSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) return;

        if (args[0].equalsIgnoreCase("-s")) {
            //TODO: Start server
        } else if (args[0].equalsIgnoreCase("-c")) {
            //TODO: Start client
        } else {
            System.out.println("Command not recognized - Stopping application");
        }

        //Testing marshalling
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
            if (re.getID() == r.getID()) System.out.println("Marshalling Ok");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Testing Server Socket & Client Socket
        //ClientMessage(ClientMessageType type, int id, InetAddress ip, int port, InetAddress receiverIp, int receiverPort, Object requestData)
        //ServerMessage(InetAddress ip, int port, InetAddress receiverIp, int receiverPort, int requestID, Object data){

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
                System.out.println("Client socket send request correctly");
                ServerMessage rep = new ServerMessage(clientClientMessage.GetIPAddress(), clientClientMessage.GetPort(),
                        clientClientMessage.getReceiverIp(), clientClientMessage.getReceiverPort(),
                        clientClientMessage.getID(), new Game(1, "Host", "Visitor"));
                server.Send(rep);
                ServerMessage serverServerMessage = client.GetMessage();
                if (serverServerMessage.getRequestID() == req.getID()) {
                    System.out.println("Server socket send reply correctly");
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
}
