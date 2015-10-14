package HockeyLive.Server;

import HockeyLive.Common.Models.Bet;

import java.net.InetAddress;

/**
 * Created by Benoit on 2015-10-14.
 */
public class BetInfo {
    private Bet bet;
    private InetAddress betterIp;
    private int betterPort;

    public Bet getBet() { return bet; }
    public void setBet(Bet bet) { this.bet = bet; }

    public InetAddress getBetterIp() { return betterIp; }
    public void setBetterIp(InetAddress betterIp) { this.betterIp = betterIp; }

    public int getBetterPort() { return betterPort; }
    public void setBetterPort(int betterPort) { this.betterPort = betterPort; }
}
