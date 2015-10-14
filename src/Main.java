import HockeyLive.Client.Communication.ClientSocket;
import HockeyLive.Common.Communication.Reply;
import HockeyLive.Common.Communication.Request;
import HockeyLive.Common.Communication.RequestType;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.Game;
import HockeyLive.Server.Communication.ServerSocket;

import java.io.IOException;
import java.net.InetAddress;

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
/*        Request r = new Request();
        r.s = "Testing";
        byte[] arr;
        try {
            arr = SerializationHelper.deserialize(r);
            Request re = (Request) SerializationHelper.deserialize(arr);
            if(re.s.equalsIgnoreCase(r.s)) System.out.println("Marshalling Ok");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        // Testing Server Socket & Client Socket
        //Request(RequestType type, int id, InetAddress ip, int port, InetAddress receiverIp, int receiverPort, Object requestData)
        //Reply(InetAddress ip, int port, InetAddress receiverIp, int receiverPort, int requestID, Object data){

        ClientSocket client = null;
        ServerSocket server = null;
        try {
            client = new ClientSocket(Constants.CLIENT_COMM_PORT);
            server = new ServerSocket(Constants.SERVER_COMM_PORT);

            Request req = new Request(RequestType.GetMatches, 2, InetAddress.getByName("localhost"),
                    Constants.SERVER_COMM_PORT, InetAddress.getByName("localhost"), Constants.CLIENT_COMM_PORT, null);
            client.SendRequest(req);
            Request clientRequest = server.GetRequest();

            if (clientRequest.getID() == req.getID()) {
                System.out.println("Client socket send request correctly");
                Reply rep = new Reply(clientRequest.getReceiverIp(), clientRequest.getReceiverPort(),
                        InetAddress.getByName("localhost"),  Constants.SERVER_COMM_PORT,
                        clientRequest.getID(), new Game());
                server.SendReply(rep);
                Reply serverReply = client.GetReply();
                if (serverReply.getRequestID() == req.getID()) {
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
