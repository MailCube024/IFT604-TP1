package HockeyLive.Common.Communication;

import java.io.Serializable;
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
