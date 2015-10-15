package HockeyLive.Client;

import HockeyLive.Client.Communication.ClientSocket;
import HockeyLive.Common.Communication.ClientMessage;
import HockeyLive.Common.Communication.ClientMessageType;
import HockeyLive.Common.Communication.ServerMessage;
import HockeyLive.Common.Constants;
import HockeyLive.Common.Models.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
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

    private ClientSocket socket;
    private Game SelectedGame;

    public ClientForm() {
        /**For test purpose creating a List with stuff in it. REMOVE AFTER TEST IS DONE.**/
        /*ArrayList<Game> testListGame = new ArrayList<Game>();
        ArrayList<GameInfo> testListGameInfo = new ArrayList<GameInfo>();

        testListGame.add(new Game(1, "Montreal", "Ottawa"));
        GameInfo info1 = new GameInfo(1, 20);
        info1.addHostGoals(new Goal("Montreal player #56"));
        info1.addHostGoals(new Goal("Montreal player #56"));
        info1.addHostGoals(new Goal("Montreal player #32"));
        info1.addVisitorGoals(new Goal("Ottawa player #87"));
        info1.addHostPenalties(new Penalty("Montreal player #45", Duration.ofMinutes(2)));
        info1.addVisitorPenalties(new Penalty("Ottawa player #22", Duration.ofMinutes(10)));
        info1.addVisitorPenalties(new Penalty("Ottawa player #53", Duration.ofMinutes(2)));
        testListGameInfo.add(info1);

        testListGame.add(new Game(2, "Vancouver", "Calgary"));
        testListGameInfo.add(new GameInfo(2, 10));
        testListGame.add(new Game(3, "San-Jose", "St-Louis"));
        testListGameInfo.add(new GameInfo(3, 10));
        MatchList.setListData(testListGame.toArray());*/

        /****************************************************************************/

        MatchList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (MatchList.getSelectedIndex() != -1){
                    cmdRefresh.setEnabled(true);
                    cmdPlaceBet.setEnabled(true);
                    SelectedGame = (Game) MatchList.getSelectedValue();
                    txtHostName.setText(SelectedGame.getHost());
                    txtVisitorName.setText(SelectedGame.getVisitor());

                    /********************************************************************************************/
                    //Normally we will make a request with the GameID here.
                    //Create a thread for the request.
                    //And receive de GameInfo.
                    /********************************************************************************************/

                    /**Test GameInfo get here. Remove this after test is done. Keep some logic for the thread refresh**/
                    GameInfo selectedGameInfo;

                    /*for (GameInfo gi : testListGameInfo) {
                        if (gi.getGameID() == SelectedGame.getGameID()) {
                            selectedGameInfo = gi;
                            txtPeriod.setText(String.valueOf(selectedGameInfo.getPeriod()));

                            String minutes = String.valueOf(selectedGameInfo.getPeriodChronometer().getSeconds() / 60);
                            String seconds = String.format("%02d", selectedGameInfo.getPeriodChronometer().getSeconds() % 60);

                            txtTimer.setText(String.format("%s:%s", minutes, seconds));
                            txtHostGoals.setText(String.valueOf(selectedGameInfo.getHostGoalsTotal()));
                            txtVisitorGoals.setText(String.valueOf(selectedGameInfo.getVisitorGoalsTotal()));
                            HostScorerList.setListData(selectedGameInfo.getHostGoals().toArray());
                            VisitorScorerList.setListData(selectedGameInfo.getVisitorGoals().toArray());
                            HostPenaltiesList.setListData(selectedGameInfo.getHostPenalties().toArray());
                            VisitorPenaltiesList.setListData(selectedGameInfo.getVisitorPenalties().toArray());
                            break;
                        }
                    }*/
                    /**********************************************/
                }
            }
        });

        cmdRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*********************************************************************/
                //Execute a request to the server for a refresh of the GameInfo.
                //Reset automatic refresh timer.
                /*********************************************************************/
            }
        });

        cmdPlaceBet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Double amount = Double.valueOf(txtBetAmount.getText().equals("") ? "0" : txtBetAmount.getText());
                if(amount != 0){
                    if(HostRadioButton.isSelected()){

                        /**********************************/
                        //Execute request for bet.
                        /**********************************/

                        Bet newBet = new Bet(amount, SelectedGame.getHost(), SelectedGame.getGameID());
                        System.out.println("You just bet on the host team.");
                    } else if (VisitorRadioButton.isSelected()){

                        /**********************************/
                        //Execute request for bet.
                        /**********************************/

                        Bet newBet = new Bet(amount, SelectedGame.getVisitor(), SelectedGame.getGameID());
                        System.out.println("You just bet on the visitor team.");

                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a team to bet on.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter an amount to bet.");
                }
                //Execute a request for a bet.
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClientForm");
        ClientForm form = new ClientForm();
        frame.setContentPane(form.MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        /****************************************************************************/
        //Envoie d'une request au serveur pour la liste des matches du jour.
        //Au retour, binder la liste des matches.
        /****************************************************************************/
        ArrayList<Game> gameList = Client.RequestGameList();
        form.MatchList.setListData(gameList.toArray());
    }

    private void createUIComponents() {
        MainPanel = new JPanel();
    }
}
