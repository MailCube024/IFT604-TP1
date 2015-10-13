package HockeyLive.Common.Communication;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by Michaël on 10/12/2015.
 */
public class Reply implements Serializable {
    private InetAddress senderIp;
    private int senderPort;
    private InetAddress receiverIp;
    private int receiverPort;
    private int requestID;
    private Object data;

    public Reply(InetAddress ip, int port, InetAddress receiverIp, int receiverPort, int requestID, Object data){
        senderIp = ip;
        senderPort = port;
        this.receiverIp = receiverIp;
        this.receiverPort = receiverPort;
        this.requestID = requestID;
        this.data = data;
    }

    public InetAddress GetIPAddress(){
        return senderIp;
    }

    public int GetPort() {
        return senderPort;
    }

    public Object getData() {
        return data;
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
}
