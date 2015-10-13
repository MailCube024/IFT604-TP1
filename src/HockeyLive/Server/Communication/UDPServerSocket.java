package HockeyLive.Server.Communication;

import HockeyLive.Common.Communication.Reply;
import HockeyLive.Common.Communication.Request;
import HockeyLive.Common.helpers.SerializationHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Michaël on 10/12/2015.
 */
public class UDPServerSocket {
    private DatagramSocket epSocket;
    private Thread tReceive;
    private BlockingQueue<Request> requestBuffer = new ArrayBlockingQueue<Request>(50);

    public UDPServerSocket(int port) throws IOException {
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
                epSocket.receive(packet);
                Request req = (Request) SerializationHelper.deserialize(packet.getData());
                requestBuffer.add(req);
            } catch (Exception e) {
                e.printStackTrace();
                CloseSocket();
            }
        }
    }

    public void SendReply(Reply reply) {
        try {
            byte[] data = SerializationHelper.serialize(reply);
            DatagramPacket packet = new DatagramPacket(data, data.length, reply.GetIPAddress(), reply.GetPort());
            DatagramSocket replySocket = new DatagramSocket();
            replySocket.send(packet);
            replySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetRequest() throws InterruptedException {
        requestBuffer.take();
    }

    private void CloseSocket() {
        if (epSocket.isConnected())
            epSocket.close();
    }

    @Override
    protected void finalize() throws Throwable {
        tReceive.interrupt();
        super.finalize();
    }
}
