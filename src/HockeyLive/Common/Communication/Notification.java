package HockeyLive.Common.Communication;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by Michaël on 10/14/2015.
 */
public class Notification implements Serializable {
    private InetAddress senderIp;
    private int senderPort;
    private InetAddress receiverIp;
    private int receiverPort;
    private int requestID;
    private Object data;

    public Notification(InetAddress senderIp, int senderPort, InetAddress receiverIp, int receiverPort, int requestID, Object data) {
        this.senderIp = senderIp;
        this.senderPort = senderPort;
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

    public int getRequestID() {
        return requestID;
    }

    public Object getData() {
        return data;
    }
}
