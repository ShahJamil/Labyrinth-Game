package labyrinthgame;

/**
 *
 * @author cdwcrz
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

// creates frame for highscore table, it displays the scores of the players
public class HighScoreFrame extends JFrame {
    public HighScoreFrame(ArrayList<HighScore> highScores) {
        HighScorePanel scorePanel = new HighScorePanel(highScores);
        add(scorePanel);
        setTitle("High Scores");
        setSize(300, 400); // Set the size of the window
        setLocationRelativeTo(null); // Center the window
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true);
    }
}
// creation of highscorepanel, it has two variables: scoreTable, tableModel
class HighScorePanel extends JPanel {
    private JTable scoreTable;
    private DefaultTableModel tableModel;
    
    /**
     * Constructor of the class
     * @param highScores 
     */
    public HighScorePanel(ArrayList<HighScore> highScores) {
        setLayout(new BorderLayout());
        String[] columnNames = {"Player Name", "Score", "Time"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (HighScore score : highScores) {
            tableModel.addRow(new Object[]{score.getName(), score.getScore(), score.getTime()});
        }

        scoreTable = new JTable(tableModel);
        scoreTable.setFont(new Font("Serif", Font.PLAIN, 18)); // Increase font size
        scoreTable.setRowHeight(30); // Increase row height

        add(new JScrollPane(scoreTable), BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
    }
}
