package HockeyLive.Common.Models;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

/**
 * Created by Michaël on 10/12/2015.
 */
public class GameInfo implements Serializable {
    public int GameID;
    public Duration PeriodChronometer;
    public int Period;
    public List<Penalty> Penalties;
}
