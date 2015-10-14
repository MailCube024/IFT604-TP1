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
 * Created by Michael on 10/12/2015.
 */
public class ServerSocket {
    private DatagramSocket epSocket;
    private Thread tReceive;
    private BlockingQueue<Request> requestBuffer = new ArrayBlockingQueue<>(50);

    public ServerSocket(int port) throws IOException {
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
                if (tReceive.isInterrupted()) break;
                epSocket.receive(packet);
                Request req = (Request) SerializationHelper.deserialize(packet.getData());
                requestBuffer.put(req);
            } catch (Exception e) {
                CloseSocket();
                break;
            }
        }
    }

    public void SendReply(Reply reply) {
        try {
            byte[] data = SerializationHelper.serialize(reply);
            DatagramPacket packet = new DatagramPacket(data, data.length, reply.getReceiverIp(), reply.getReceiverPort());
            epSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Request GetRequest() throws InterruptedException {
        return requestBuffer.take();
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
