package labyrinthgame;

/**
 *
 * @author cdwcrz
 */


public class Labyrinth {
    
    public Player player;
    public Dragon dragon;
    
    /**
     * instantiation of the Labyrinth class
     *
     * @param matrix
     */
    public Labyrinth(int[][] matrix){
        this.player = new Player(matrix);
        this.dragon = new Dragon(matrix);
    }


}
