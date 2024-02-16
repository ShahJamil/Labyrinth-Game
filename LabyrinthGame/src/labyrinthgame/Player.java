package labyrinthgame;

/**
 *
 * @author cdwcrz
 */

import java.awt.event.KeyEvent;

public class Player {
    public int[][] matrix;
    public int playerX; 
    public int playerY; 
    
    /**
     * The constructor of the player
     *
     * @param matrix
     */
    public Player(int[][] matrix) {
        this.matrix = matrix;
        this.playerX = 1; 
        this.playerY = matrix.length - 2; 
    }
    
    /**
     * The method for making the player move in 4 directions: WASD
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_W: 
                movePlayer(0, -1);
                break;
            case KeyEvent.VK_S: 
                movePlayer(0, 1);
                break;
            case KeyEvent.VK_A:
                movePlayer(-1, 0);
                break;
            case KeyEvent.VK_D:
                movePlayer(1, 0);
                break;
        }
    }

    public void keyTyped(KeyEvent e) { }

    public void keyReleased(KeyEvent e) { }

    /**
     * When the key is pressed this method makes the player move in that direction and changes it's place in matrix
     * It has two params, which are horizontal and vertical positions of the player
     * @param x,y
     */
    public void movePlayer(int x, int y) {
        if (this.playerY + y >= 0 && this.playerY + y < this.matrix.length && 
            this.playerX + x >= 0 && this.playerX + x < this.matrix[0].length && 
            this.matrix[this.playerY + y][this.playerX + x] == 0 || this.matrix[this.playerY + y][this.playerX + x] == 6) {
            this.playerX += x;
            this.playerY += y;
        }
    }

    /**
     * Method resets the position of the player.
     *
     * @noparams
     */
    public void reset(){
        this.playerX = 1;
        this.playerY = matrix.length - 2;
    }
    /**
     * Method checks if the player is next to dragon, it takes dragon's positions as a parameter
     *
     * @param x,y
     * @return boolean
     */
    public boolean isNextToDragon(int x, int y) {
        int distX = Math.abs(this.playerX - x);
        int distY = Math.abs(this.playerY - y);
        if (distX <= 1 && distY == 0 || (distX == 0 && distY <= 1)) {
            return true;
        }
        return false;
    }

    /**
     * Method to check if the player won the level
     * no params
     * @return boolean
     */
    public boolean isWin(){
        return this.matrix[playerY][playerX] == 6;
    }
    
}
