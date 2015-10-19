package HockeyLive.Client;

import HockeyLive.Client.Communication.Client;
import HockeyLive.Client.Listeners.BetConfirmationListener;
import HockeyLive.Client.Listeners.BetUpdateListener;
import HockeyLive.Client.Listeners.GameInfoUpdateListener;
import HockeyLive.Client.Listeners.GameListUpdateListener;
import HockeyLive.Client.Refresh.GameInfoRefresher;
import HockeyLive.Common.Models.Bet;
import HockeyLive.Common.Models.Game;
import HockeyLive.Common.Models.GameInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Michaël Beaulieu         13048132
 * Benoit Jeunehomme        13055392
 * Bruno-Pier Touchette     13045732
 */
public class ClientForm implements GameInfoUpdateListener, GameListUpdateListener, BetConfirmationListener, BetUpdateListener {
    private final int PERIODIC_REFRESH = 2; // in minutes
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
    private JLabel txtGain;
    private JPanel GainPanel;
    private Game SelectedGame;

    private Client client;
    private GameInfoRefresher refresher;

    private HashMap<Integer, Double> betGains = new HashMap<>();
    private List<Bet> betsReceived = new ArrayList<>();

    public ClientForm() {

        MatchList.addListSelectionListener(e -> {
            if (MatchList.getSelectedIndex() != -1) {
                cmdRefresh.setEnabled(true);
                cmdPlaceBet.setEnabled(true);
                SelectedGame = (Game) MatchList.getSelectedValue();
                refresher.UpdateSelectedGame(SelectedGame.getGameID());
                txtHostName.setText(SelectedGame.getHost());
                txtVisitorName.setText(SelectedGame.getVisitor());

                /********************************************************************************************/
                //Normally we will make a request with the GameID here.
                //Create a thread for the request.
                //And receive the GameInfo.
                /********************************************************************************************/

                client.RequestGameInfo(SelectedGame.getGameID());
            }
        });

        /*********************************************************************/
        //Execute a request to the server for a refresh of the GameInfo.
        //Reset automatic refresh timer.
        /*********************************************************************/
        cmdRefresh.addActionListener(e -> {
            client.RequestGameInfo(SelectedGame.getGameID());
            refresher.Reset();
        });

        cmdPlaceBet.addActionListener(e -> {
            Double amount = Double.valueOf(txtBetAmount.getText().equals("") ? "0" : txtBetAmount.getText());
            if (amount != 0) {
                if (HostRadioButton.isSelected() || VisitorRadioButton.isSelected()) {
                    boolean hostSelected = HostRadioButton.isSelected();
                    Bet newBet = new Bet(amount, hostSelected ? SelectedGame.getHost() : SelectedGame.getVisitor(), SelectedGame.getGameID());
                    client.SendBet(newBet);
                    System.out.println(String.format("You just bet on the %s team.", (hostSelected ? " host " : " visitor ")));
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a team to bet on.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter an amount to bet.");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClientForm");
        ClientForm form = new ClientForm();
        form.InitializeClient();
        form.InitializeRefresher();
        frame.setContentPane(form.MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                form.Close();
                System.exit(0);
            }
        });

        form.RequestGameList();
        frame.setVisible(true);
    }

    public void InitializeClient() {
        client = new Client();
        client.AddGameListUpdateListener(this);
        client.AddGameInfoUpdateListener(this);
        client.AddBetConfirmationListener(this);
        client.AddBetUpdateListener(this);
        client.Start();
    }

    private void InitializeRefresher() {
        refresher = new GameInfoRefresher(PERIODIC_REFRESH, client);
    }

    private void Close() {
        client.UnregisterListeners();
        refresher.Stop();
        client.Close();
    }

    private void RequestGameList() {
        client.RequestGameList();
    }

    private void createUIComponents() {
        MainPanel = new JPanel();
    }

    @Override
    public void UpdateGameInfo(GameInfo info) {
        EventQueue.invokeLater(() -> {
            txtPeriod.setText(String.valueOf(info.getPeriod()));

            txtTimer.setText(info.getPeriodFormattedChronometer());
            txtHostGoals.setText(String.valueOf(info.getHostGoalsTotal()));
            txtVisitorGoals.setText(String.valueOf(info.getVisitorGoalsTotal()));
            HostScorerList.setListData(info.getHostGoals().toArray());
            VisitorScorerList.setListData(info.getVisitorGoals().toArray());
            HostPenaltiesList.setListData(info.getHostPenalties().toArray());
            VisitorPenaltiesList.setListData(info.getVisitorPenalties().toArray());

            if(info.getPeriod() == 3) {
                betGains.putIfAbsent(info.getGameID(), 0.0);
                BetPanel.setVisible(false);
                GainPanel.setVisible(true);
                if (info.getPeriodChronometer() == 0) {
                    txtGain.setText("Gain : " + betGains.get(info.getGameID()));
                }
            }
        });
    }

    @Override
    public void UpdateGameList(List<Game> games) {
        EventQueue.invokeLater(() -> MatchList.setListData(games.toArray()));
    }

    @Override
    public void IsBetConfirmed(boolean betConfirmation) {
        if(betConfirmation) {
            txtBetAmount.setText("");
        }
    }

    @Override
    public void BetUpdate(Bet bet) {
        if(! betsReceived.contains(bet)) {
            betsReceived.add(bet);
            Double oldGain = betGains.getOrDefault(bet.getGameID(), 0.0);
            betGains.put(bet.getGameID(), oldGain + bet.getAmountGained());
        }
    }
}
