package HockeyLive.Client.Communication;

import HockeyLive.Common.Communication.Reply;
import HockeyLive.Common.Communication.Request;
import HockeyLive.Common.helpers.SerializationHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Michaël on 10/12/2015.
 */
public class ClientSocket {
    private DatagramSocket epSocket;
    private Thread tReceive;
    private BlockingQueue<Reply> replyBuffer = new ArrayBlockingQueue<>(50);

    public ClientSocket(int port) throws IOException {
        epSocket = new DatagramSocket(port);
        tReceive = new Thread(() -> {
            Receive();
        });
        tReceive.start();
    }

    public void Receive() {
        byte[] receiveData = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
            try {
                System.out.println("Client: Receiving message");
                epSocket.receive(packet);
                Reply reply = (Reply) SerializationHelper.deserialize(packet.getData());
                replyBuffer.add(reply);
            } catch (Exception e) {
                e.printStackTrace();
                CloseSocket();
            }
            if(tReceive.isInterrupted()) break;
        }
    }

    public void SendRequest(Request request) {
        try {
            byte[] data = SerializationHelper.serialize(request);
            DatagramPacket packet = new DatagramPacket(data, data.length, request.GetIPAddress(), request.GetPort());
            DatagramSocket requestSocket = new DatagramSocket();
            requestSocket.send(packet);
            requestSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Reply GetReply() throws InterruptedException {
        return replyBuffer.take();
    }

    public void CloseSocket() {
        if (epSocket.isConnected())
            epSocket.close();
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Finalizing Server Socket");
        tReceive.interrupt();
        super.finalize();
    }
}
