package labyrinthgame;

import java.awt.Point;
import java.util.Random;

/**
 *
 * @author cdwcrz
 */

public class Dragon {
    private int[][] matrix;
    private Random random = new Random();
    public int dragonX;
    public int dragonY;
    private int currentDirection;
    /**
     * instantiation of this class, it takes matrix level as a parameter and sets the dragon in the given position
     * @param matrix 
     */
    public Dragon(int[][] matrix) {
        this.matrix = matrix;
        int x;
                    Random rand = new Random();
        do{
            x = rand.nextInt(matrix[0].length);
        }while(this.matrix[1][x] != 0);
        this.dragonX = x;
        this.dragonY = 1;
        currentDirection = random.nextInt(4);
    }
    
    /**
     * This method makes the dragon move in one chosen direction which is defined in the if statement. When dragon reaches the wall it changes the direction
     * no params
     */
    public void moveRandomly() {
        int[] dx = {-1, 1, 0, 0}; 
        int[] dy = {0, 0, -1, 1};

        int newX = this.dragonX + dx[currentDirection];
        int newY = this.dragonY + dy[currentDirection];

        if (newX < 0 || newX >= matrix[0].length || newY < 0 || newY >= matrix.length || matrix[newY][newX] == 1) {
            currentDirection = random.nextInt(4);
        } else {
            this.dragonX = newX;
            this.dragonY = newY;
        }
        
    }
    /**
     * this method checks if the dragon is next to player, it takes into account the player's position
     * @param playerPosition
     */
    public boolean isNextToPlayer(Point playerPosition) {
        int distX = Math.abs(this.dragonX - playerPosition.x);
        int distY = Math.abs(this.dragonY - playerPosition.y);
        if (distX <= 1 && distY == 0 || (distX == 0 && distY <= 1)) {
            return true;
        }
        return false;
    }
    /**
     * this method resets the position of the dragon
     * no params
     */
    public void reset(){
        
        int x;
                    Random rand = new Random();
        do{
            x = rand.nextInt(matrix[0].length);
        }while(this.matrix[1][x] != 0);
        this.dragonX = x;
        this.dragonY = 1;
        
    }
}
