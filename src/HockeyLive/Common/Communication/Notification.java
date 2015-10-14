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
}
