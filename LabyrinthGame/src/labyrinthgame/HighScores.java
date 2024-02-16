/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthgame;

import labyrinthgame.HighScore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

/**
 *
 * @author cdwcrz
 */
public class HighScores {

    int maxScores;
    PreparedStatement insertStatement;
    PreparedStatement deleteStatement;
    Connection connection;

    public HighScores(int maxScores) throws SQLException {
        this.maxScores = maxScores;
        Properties connectionProps = new Properties();
        // Add new user -> MySQL workbench (Menu: Server / Users and priviliges)
        //                             Tab: Administrative roles -> Check "DBA" option
        connectionProps.put("user", "jamilgame");
        connectionProps.put("password", "labgame12@");
        connectionProps.put("serverTimezone", "UTC");
        String dbURL = "jdbc:mysql://localhost:3306/highscores";
        connection = DriverManager.getConnection(dbURL, connectionProps);
        
        
        String insertQuery = "INSERT INTO HIGHSCORES (NAME, SCORE, TIME) VALUES (?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        String score = "1; DELETE FROM HIGHSCORES;";
        String deleteQuery = "DELETE FROM HIGHSCORES WHERE SCORE=?"; // + score;
        deleteStatement = connection.prepareStatement(deleteQuery);
    }
/**
     * getter of highScores
     * no params
     */
    public ArrayList<HighScore> getHighScores() throws SQLException {
        String query = "SELECT * FROM HIGHSCORES ORDER BY SCORE DESC";
        ArrayList<HighScore> highScores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            int time = results.getInt("TIME");
            highScores.add(new HighScore(name, score, time));
        }
        sortHighScores(highScores);
        return highScores;
    }
    
    
    /**
     * inserting the datas and removing the unnecessary ones
     * @param name, score, time 
     */
    public void putHighScore(String name, int score, int time) throws SQLException {
        ArrayList<HighScore> highScores = getHighScores();
        if (highScores.size() < maxScores) {
            insertScore(name, score, time);
        } else {
            int leastScore = highScores.get(highScores.size() - 1).getScore();
            if (leastScore < score) {
                deleteScores(leastScore);
                insertScore(name, score, time);
            }
        }
    }

    /**
     * Sort the high scores in descending order.
     * @param highScores 
     */
    private void sortHighScores(ArrayList<HighScore> highScores) {
    Collections.sort(highScores, new Comparator<HighScore>() {
        @Override
        public int compare(HighScore h1, HighScore h2) {
            int scoreCompare = Integer.compare(h2.getScore(), h1.getScore());
            if (scoreCompare == 0) {
                return Integer.compare(h1.getTime(), h2.getTime());
            }
            return scoreCompare;
        }
    });
}
    /**
     * inserting scores
     * @param highScores 
     */
    private void insertScore(String name, int score, int time) throws SQLException {
        insertStatement.setString(1, name);
        insertStatement.setInt(2, score);
        insertStatement.setInt(3, time);

        insertStatement.executeUpdate();
    }

    /**
     * Deletes all the highscores with score.
     *
     * @param score
     */
    private void deleteScores(int score) throws SQLException {
        deleteStatement.setInt(1, score);
        deleteStatement.executeUpdate();
    }
}
