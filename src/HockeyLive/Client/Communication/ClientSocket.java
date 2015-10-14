package HockeyLive.Client.Communication;

import HockeyLive.Common.Communication.Reply;
import HockeyLive.Common.Communication.Request;
import HockeyLive.Common.helpers.SerializationHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Micha�l on 10/12/2015.
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
                if (tReceive.isInterrupted()) break;
                epSocket.receive(packet);
                Reply reply = (Reply) SerializationHelper.deserialize(packet.getData());
                replyBuffer.put(reply);
            } catch (Exception e) {
                CloseSocket();
                break;
            }
        }
    }

    public void SendRequest(Request request) {
        try {
            byte[] data = SerializationHelper.serialize(request);
            DatagramPacket packet = new DatagramPacket(data, data.length, request.GetIPAddress(), request.GetPort());
            epSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Reply GetReply() throws InterruptedException {
        return replyBuffer.take();
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
