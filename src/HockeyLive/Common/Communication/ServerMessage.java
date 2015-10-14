package HockeyLive.Common.Communication;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by Michaël on 10/12/2015.
 */
public class ServerMessage implements Serializable {

    private InetAddress senderIp;
    private int senderPort;
    private InetAddress receiverIp;
    private int receiverPort;
    private int requestID;
    private Object data;

    public ServerMessage(InetAddress ip, int port, InetAddress receiverIp, int receiverPort, int requestID, Object data){
        senderIp = ip;
        senderPort = port;
        this.receiverIp = receiverIp;
        this.receiverPort = receiverPort;
        this.requestID = requestID;
        this.data = data;
    }

    public InetAddress getSenderIp() {
        return senderIp;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public InetAddress getReceiverIp() {
        return receiverIp;
    }

    public int getReceiverPort() {
        return receiverPort;
    }

    public Object getData() {
        return data;
    }

    public int getRequestID() {
        return requestID;
    }



}
