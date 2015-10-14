package HockeyLive.Client;

import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;

/**
 * Created by Bruno-Pier on 2015-10-12.
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
    private JTextArea txtVisitorScorer;
    private JTextArea txtHostScorer;
    private JTextArea txtHostPenalties;
    private JTextArea txtVisitorPenalties;
    private JPanel BetPanel;
    private JRadioButton HostRadioButton;
    private JRadioButton VisitorRadioButton;
    private JButton cmdPlaceBet;
    private JPanel PenaltiesVisitorPanel;
    private JPanel PenaltiesHostPanel;
    private JTextField txtBetAmount;
    private JButton cmdRefresh;

    public ClientForm() {
        /**For test purpose creating a List with stuff in it.**/
        ArrayList<Game> testListGame = new ArrayList<Game>();
        ArrayList<GameInfo> testListGameInfo = new ArrayList<GameInfo>();

        testListGame.add(new Game(1, "Montreal", "Ottawa"));
        testListGameInfo.add(new GameInfo(1));
        testListGame.add(new Game(2, "Vancouver", "Calgary"));
        testListGameInfo.add(new GameInfo(2));
        testListGame.add(new Game(3, "San-Jose", "St-Louis"));
        testListGameInfo.add(new GameInfo(3));
        MatchList.setListData(testListGame.toArray());

        /****************************************************************************/

        MatchList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

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
        MatchList = new JList();
        MatchInfoPanel = new JPanel();
        txtHostName = new JTextField();
        txtVisitorName = new JTextField();
        PeriodPanel = new JPanel();
        txtPeriod = new JTextField();
        TimerPanel = new JPanel();
        txtTimer = new JTextField();
        ScoreTeam1Panel = new JPanel();
        ScoreTeam2Panel = new JPanel();
        txtHostGoals = new JTextField();
        txtVisitorGoals = new JTextField();
        txtVisitorScorer = new JTextArea();
        txtHostScorer = new JTextArea();
        txtHostPenalties = new JTextArea();
        txtVisitorPenalties = new JTextArea();
        BetPanel = new JPanel();
        HostRadioButton = new JRadioButton();
        VisitorRadioButton = new JRadioButton();
        cmdPlaceBet = new JButton();
        PenaltiesVisitorPanel = new JPanel();
        PenaltiesHostPanel = new JPanel();
        txtBetAmount = new JTextField();
        cmdRefresh = new JButton();
    }
}
