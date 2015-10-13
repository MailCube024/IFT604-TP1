package HockeyLive.Common.Communication;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by Michaël on 10/12/2015.
 */
public class Request implements Serializable {
    private RequestType type;

    private InetAddress senderIp;
    private int senderPort;
    private InetAddress receiverIp;
    private int receiverPort;
    private int ID;
    private Object requestData;

    public Request(RequestType type, int id, InetAddress ip, int port, InetAddress receiverIp, int receiverPort, Object requestData){
        this.type = type;
        senderIp = ip;
        senderPort = port;
        ID = id;
        this.receiverIp = receiverIp;
        this.receiverPort = receiverPort;
        this.requestData = requestData;
    }

    public InetAddress GetIPAddress() {
        return senderIp;
    }

    public int GetPort() {
        return senderPort;
    }

    public RequestType getType() {
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

    public Object getRequestData() {
        return requestData;
    }
}
