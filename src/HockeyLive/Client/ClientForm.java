package HockeyLive.Client;

import javax.swing.*;

/**
 * Created by Bruno-Pier on 2015-10-12.
 */
public class ClientForm {
    private JPanel MainPanel;
    private JList MatchList;
    private JPanel MatchInfoPanel;
    private JTextField txtTeamName1;
    private JTextField txtTeamName2;
    private JPanel PeriodPanel;
    private JTextField txtPeriod;
    private JPanel TimerPanel;
    private JTextField txtTimer;
    private JPanel ScoreTeam1Panel;
    private JPanel ScoreTeam2Panel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextArea txtTeam2Scorer;
    private JTextArea txtTeam1Scorer;
    private JTextArea txtTeam1Penalties;
    private JTextArea txtTeam2Penalties;
    private JPanel BetPanel;
    private JRadioButton team1RadioButton;
    private JRadioButton team2RadioButton;
    private JButton placeYouBetButton;
    private JPanel PenaltiesTeam2Panel;
    private JPanel PenaltiesTeam1Panel;
    private JTextField txtBetAmount;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClientForm");
        frame.setContentPane(new ClientForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        MainPanel = new JPanel();
        MatchList = new JList();
        MatchInfoPanel = new JPanel();
        txtTeamName1 = new JTextField();
        txtTeamName2 = new JTextField();
        PeriodPanel = new JPanel();
        txtPeriod = new JTextField();
        TimerPanel = new JPanel();
        txtTimer = new JTextField();
        ScoreTeam1Panel = new JPanel();
        ScoreTeam2Panel = new JPanel();
        textField1 = new JTextField();
        textField2 = new JTextField();
        txtTeam2Scorer = new JTextArea();
        txtTeam1Scorer = new JTextArea();
        txtTeam1Penalties = new JTextArea();
        txtTeam2Penalties = new JTextArea();
        BetPanel = new JPanel();
        team1RadioButton = new JRadioButton();
        team2RadioButton = new JRadioButton();
        placeYouBetButton = new JButton();
        PenaltiesTeam2Panel = new JPanel();
        PenaltiesTeam1Panel = new JPanel();
        txtBetAmount = new JTextField();
    }
}
