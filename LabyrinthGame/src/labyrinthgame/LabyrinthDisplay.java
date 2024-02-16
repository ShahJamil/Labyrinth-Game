package labyrinthgame;

/**
 *
 * @author cdwcrz
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LabyrinthDisplay extends JFrame implements KeyListener {

    private LabyrinthPanel labyrinthPanel;
    public int currentLevel = 0;
    public int[][][] levels;

     /**
     * Constructor of LabyrinthDisplay, it adds menubar with leaderboard and newgame section
     *
     * @param matrices
     */
    public LabyrinthDisplay(int[][][] matrices) throws IOException, SQLException {
        this.levels = matrices;
        labyrinthPanel = new LabyrinthPanel(matrices);
        add(labyrinthPanel);
        pack();
        setTitle("Labyrinth Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true);
      
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenu leaderboards = new JMenu("LeaderBoard");
        menuBar.add(leaderboards);
        JMenuItem newGame = new JMenuItem("New game");
        JMenuItem leaderboard = new JMenuItem("leaderBoard");
        newGame.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    labyrinthPanel.finishGame();
                } catch (SQLException ex) {
                    Logger.getLogger(LabyrinthDisplay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        leaderboard.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HighScoreFrame hsf = new HighScoreFrame(labyrinthPanel.highscore.getHighScores());
                } catch (SQLException ex) {
                    Logger.getLogger(LabyrinthDisplay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        gameMenu.add(newGame);
        leaderboards.add(leaderboard);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        labyrinthPanel.keyPressed(e);
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    
}
    

class LabyrinthPanel extends JPanel {

    public Labyrinth labyrinth;
    public int cellSize = 50; 
    public Image wallImage;
    public Image playerImage;
    public Image dragonImage;
    public Image groundImage;
    public Image fogImage;
    public Timer moveTimer;
    public boolean won;
    public int currlevel;
    public int[][][] levels;
    public ArrayList<Integer> scores = new ArrayList<>();
    public boolean finishGame = false;
    public long elapsedMillis;
    private JLabel timerLabel;
    private Timer timer;
    private long startTime;
    public HighScores highscore;

    /**
     * Constructor of LabyrinthPanel, it reads images for the player, enemy and the textures of the wall and ground
     *
     * @param matrix
     */
    public LabyrinthPanel(int[][][] matrix) throws IOException, SQLException {
        this.won = false;
        this.levels = matrix;
         
        wallImage = ImageIO.read(new File("src\\labyrinthgame\\image.png"));
        playerImage = ImageIO.read(new File("src\\labyrinthgame\\player.png"));
        playerImage = playerImage.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        groundImage = ImageIO.read(new File("src\\labyrinthgame\\ground.png"));
        groundImage = groundImage.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        dragonImage = ImageIO.read(new File("src\\labyrinthgame\\cheburashka.jpg"));
        dragonImage = dragonImage.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        this.currlevel = 0;
        this.initializeGame();
        highscore = new HighScores(10);

    }

    


    
    /**
     * It resets the game and sets the currentlevel to 0
     *
     * @noparams
     */
    public void resetGame() throws SQLException{
        this.currlevel = 0;
        this.initializeGame();
    }
    
    /**
     * the method for the finishing the game, it stops the timer and sends the data of the player into highscoreframe
     *
     * @noparams
     */
    public void finishGame() throws SQLException{
        timer.stop();
//            reset2();
            String playerName = JOptionPane.showInputDialog(null, "Enter your name:", "High Score", JOptionPane.PLAIN_MESSAGE);
            highscore.putHighScore(playerName, this.currlevel, (int)(this.elapsedMillis / 1000));
            this.currlevel = 0;
                HighScoreFrame hsf = new HighScoreFrame(highscore.getHighScores());
                
           
    }

    /**
     *This method is used to initialize the level when the game starts or if the player proceeds to the next level.
     *
     * @noparams
     */
    public void initializeLevel() throws SQLException{
        if(this.currlevel == 10){
            finishGame();
        }
        else{
           this.labyrinth = new Labyrinth(levels[currlevel]);
        
        moveTimer = new Timer(1000, e -> {
        if(!finishGame){
            if (labyrinth.dragon.isNextToPlayer(new Point(labyrinth.player.playerX, labyrinth.player.playerY)) || labyrinth.player.isNextToDragon(labyrinth.dragon.dragonX, labyrinth.dragon.dragonY)) {
            
            moveTimer.stop();
            finishGame = true;
            try {
                JOptionPane.showMessageDialog(this, "You have been caught by the dragon!, your score is: " + this.currlevel);
                finishGame();
            } catch (SQLException ex) {
                Logger.getLogger(LabyrinthPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
               
            
            
            }
            else{
            labyrinth.dragon.moveRandomly();

            if(this.labyrinth.player.isWin()){
            moveTimer.stop();
            
            this.currlevel++;
            this.won = false;
                try {
                    initializeLevel();
                } catch (SQLException ex) {
                    Logger.getLogger(LabyrinthPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            } 
        
            repaint();
        }
    });
    
        if(!finishGame){
            moveTimer.start();
        }
        
    } 
    }
    /**
     * the method for the initialization of the game.
     *
     * @noparams
     */
    public void initializeGame() throws SQLException{
        timerLabel = new JLabel("Time: 0");
        timerLabel.setForeground(Color.WHITE);
        add(timerLabel, BorderLayout.SOUTH);

        startTime = System.currentTimeMillis();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedMillis = System.currentTimeMillis() - startTime;
                timerLabel.setText("Time: " + elapsedMillis / 1000 + " seconds");
            }
        });
        timer.start();

        initializeLevel();

    }
    /**
     * the method is used to reset the player's and dragon's position into initial ones.
     *
     * @noparams
     */
    public void reset2(){
         timerLabel = new JLabel("Time: 0");
        timerLabel.setForeground(Color.WHITE);

        
        this.labyrinth.player.reset();
        this.labyrinth.dragon.reset();

    }

/**
     * the method for setting the size of the window
     *
     * @noparams
     */
    public Dimension getPreferredSize() {
    int rows = levels[0].length;    
    int cols = levels[0][0].length; 

    int width = cols * cellSize;
    int height = rows * cellSize;

    return new Dimension(width, height);
}
/**
     * The method used for painting the datas: the map, player and dragon
     *
     * @noparams
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < labyrinth.player.matrix.length; i++) {
            for (int j = 0; j < labyrinth.player.matrix[i].length; j++) {
                if((this.labyrinth.player.playerX + 3 < j || this.labyrinth.player.playerY + 3 < i) || (this.labyrinth.player.playerX - 3 > j || this.labyrinth.player.playerY - 3 > i)){
                      Color fogColor = new Color(30,30,30);
                      g.setColor(fogColor);
                      g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
                else{
                  if (labyrinth.player.matrix[i][j] == 1 && wallImage != null) {
                    g.drawImage(wallImage, j * cellSize, i * cellSize, cellSize, cellSize, this);
                  }
                // } else if(labyrinth.player.matrix[this.labyrinth.player.playerY][this.labyrinth.player.playerX] == 6){
                //     this.won = true;
                // }
                 else {
                     g.drawImage(groundImage, j * cellSize, i * cellSize, cellSize, cellSize, this);
                }
                }
                
            }
        }

        g.drawImage(playerImage, labyrinth.player.playerX * cellSize, labyrinth.player.playerY * cellSize, this);
        if((this.labyrinth.player.playerX + 3 < this.labyrinth.dragon.dragonX || this.labyrinth.player.playerY + 3 < this.labyrinth.dragon.dragonY) || 
        (this.labyrinth.player.playerX - 3 > this.labyrinth.dragon.dragonX || this.labyrinth.player.playerY - 3 > this.labyrinth.dragon.dragonY)){
        }
         else{
                    g.drawImage(dragonImage, this.labyrinth.dragon.dragonX * cellSize, this.labyrinth.dragon.dragonY * cellSize, this);
        }
    }

    public void keyPressed(KeyEvent e) {
        labyrinth.player.keyPressed(e);
        repaint();
    }
}
