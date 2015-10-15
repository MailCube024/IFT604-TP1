package HockeyLive.Client;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class ClientForm {
    private JPanel MainPanel;
    private JList MatchList;
    private JPanel MatchInfoPanel;
    private JTextField txtHostName;
    private JTextField txtVisitorName;
    private JPanel PeriodPanel;
    private JTextField txtPeriod;
    private JPanel TimerPanel;
    private JTextField txtTimer;
    private JPanel ScoreTeam1Panel;
    private JPanel ScoreTeam2Panel;
    private JTextField txtHostGoals;
    private JTextField txtVisitorGoals;
    private JPanel BetPanel;
    private JRadioButton HostRadioButton;
    private JRadioButton VisitorRadioButton;
    private JButton cmdPlaceBet;
    private JPanel PenaltiesVisitorPanel;
    private JPanel PenaltiesHostPanel;
    private JTextField txtBetAmount;
    private JButton cmdRefresh;
    private JList HostPenaltiesList;
    private JList VisitorPenaltiesList;
    private JList HostScorerList;
    private JList VisitorScorerList;

    public ClientForm() {
        /**For test purpose creating a List with stuff in it.**/
        ArrayList<Game> testListGame = new ArrayList<Game>();
        ArrayList<GameInfo> testListGameInfo = new ArrayList<GameInfo>();

        testListGame.add(new Game(1, "Montreal", "Ottawa"));
        testListGameInfo.add(new GameInfo(1, 10));
        testListGame.add(new Game(2, "Vancouver", "Calgary"));
        testListGameInfo.add(new GameInfo(2, 10));
        testListGame.add(new Game(3, "San-Jose", "St-Louis"));
        testListGameInfo.add(new GameInfo(3, 10));
        MatchList.setListData(testListGame.toArray());

        /****************************************************************************/

        MatchList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (MatchList.getSelectedIndex() != -1) {
                    Game selectedGame = (Game) MatchList.getSelectedValue();
                    txtHostName.setText(selectedGame.getHost());
                    txtVisitorName.setText(selectedGame.getVisitor());

                    //Normally we will make a request with the GameID here.
                    //Create a thread for the request.
                    //And receive de GameInfo.
                    /**Test GameInfo get here.**/
                    GameInfo selectedGameInfo;

                    for (GameInfo gi : testListGameInfo) {
                        if (gi.getGameID() == selectedGame.getGameID()) {
                            selectedGameInfo = gi;
                            txtPeriod.setText(String.valueOf(selectedGameInfo.getPeriod()));
                            txtTimer.setText(String.valueOf(selectedGameInfo.getPeriodChronometer().minusSeconds(30).toMinutes()));
                            txtHostGoals.setText(String.valueOf(selectedGameInfo.getHostGoalsTotal()));
                            txtVisitorGoals.setText(String.valueOf(selectedGameInfo.getVisitorGoalsTotal()));
                            HostScorerList.setListData(selectedGameInfo.getHostGoals().toArray());
                            VisitorScorerList.setListData(selectedGameInfo.getVisitorGoals().toArray());
                            HostPenaltiesList.setListData(selectedGameInfo.getHostPenalties().toArray());
                            VisitorPenaltiesList.setListData(selectedGameInfo.getVisitorPenalties().toArray());
                            break;
                        }
                    }
                    /**********************************************/
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClientForm");
        frame.setContentPane(new ClientForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        //Envoie d'une request au serveur pour la liste des matches du jour.
        //Au retour, binder la liste des matches.

    }

    private void createUIComponents() {
        MainPanel = new JPanel();
    }
}
