package HockeyLive.Client.Communication;

import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ServerMessage;
import HockeyLive.Common.helpers.SerializationHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class ClientSocket {
    private DatagramSocket epSocket;
    private Thread tReceive;
    private BlockingQueue<ServerMessage> serverMessageBuffer = new ArrayBlockingQueue<>(50);

    public ClientSocket(int port) throws IOException {
        epSocket = new DatagramSocket(port);
        tReceive = new Thread(() -> {
            Receive();
        });
        tReceive.start();
    }

    public void Receive() {
        byte[] receiveData = new byte[4096];
        while (true) {
            DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
            try {
                if (tReceive.isInterrupted()) break;
                epSocket.receive(packet);
                ServerMessage serverMessage = (ServerMessage) SerializationHelper.deserialize(packet.getData());
                serverMessageBuffer.put(serverMessage);
            } catch (Exception e) {
                CloseSocket();
                break;
            }
        }
    }

    public void Send(ClientMessage clientMessage) {
        try {
            byte[] data = SerializationHelper.serialize(clientMessage);
            DatagramPacket packet = new DatagramPacket(data, data.length, clientMessage.GetIPAddress(), clientMessage.GetPort());
            epSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerMessage GetMessage() throws InterruptedException {
        return serverMessageBuffer.take();
    }

    public void CloseSocket() {
        tReceive.interrupt();
        epSocket.close();
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Finalizing Server Socket");
        tReceive.interrupt();
        super.finalize();
    }
}
