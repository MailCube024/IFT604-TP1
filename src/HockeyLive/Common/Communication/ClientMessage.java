package HockeyLive.Common.Communication;

import HockeyLive.Client.Communication.Client;
import HockeyLive.Common.helpers.SerializationHelper;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by Michaël on 10/12/2015.
 */
public class ClientMessage implements Serializable {
    private ClientMessageType type;

    private InetAddress senderIp;
    private int senderPort;
    private InetAddress receiverIp;
    private int receiverPort;
    private int ID;
    private Object data;

    public ClientMessage(ClientMessageType type, int id, InetAddress ip, int port, InetAddress receiverIp, int receiverPort, Object data){
        this.type = type;
        senderIp = ip;
        senderPort = port;
        ID = id;
        this.receiverIp = receiverIp;
        this.receiverPort = receiverPort;
        this.data = data;
    }

    public ClientMessage(DatagramPacket packet) throws IOException, ClassNotFoundException {
        ClientMessage temp = (ClientMessage) SerializationHelper.deserialize(packet.getData());

        this.type = temp.getType();
        this.senderIp = temp.senderIp;
        this.senderPort = temp.senderPort;
        this.ID = temp.ID;
        this.receiverIp = packet.getAddress();
        this.receiverPort = packet.getPort();
        this.data = temp.data;
    }

    public InetAddress GetIPAddress() {
        return senderIp;
    }

    public int GetPort() {
        return senderPort;
    }

    public ClientMessageType getType() {
        return type;
    }

    public InetAddress getReceiverIp() {
        return receiverIp;
    }

    public int getReceiverPort() {
        return receiverPort;
    }

    public int getID() {
        return ID;
    }

    public Object getData() {
        return data;
    }
}
