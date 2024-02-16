/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthgame;

/**
 *
 * @author cdwcrz
 */
public class HighScore {
    
    private final String name;
    private final int score;
    private final int time;
    
    /**
     * Constructor of the class
     * @param name, score, time 
     */
    public HighScore(String name, int score, int time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }
    /**
     * getter for name
     * no params
     */
    public String getName() {
        return name;
    }
    /**
     * getter for score
     * no params
     */
    public int getScore() {
        return score;
    }
    /**
     * getter for time
     * no params
     */
    public int getTime() {
        return time;
    }
    

}
