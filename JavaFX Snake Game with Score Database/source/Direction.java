import javafx.scene.Node;
/** Directions class
 * Keeps track of the snake's current direction and updates the nodes accordingly*/
public class Direction {

    /** Fields
     * Directions stores an enumeration of the snake's possible directions in the game
     * direction stores one of the Directions of the snake's current direction */
    public enum Directions {
        UP, DOWN, LEFT, RIGHT
    }
    private Directions direction = Directions.RIGHT;

    /** moveInDirection method
     * Moves the given Node to the head of the snake */
    public static void moveInDirection(Directions direction, Snake snake, Node tempSegment) {
        switch (direction) {
            case UP:
                tempSegment.setTranslateX(snake.getSnake().get(0).getTranslateX());
                tempSegment.setTranslateY(snake.getSnake().get(0).getTranslateY() - Main.BLOCK_SIZE); // Move up
                break;
            case DOWN:
                tempSegment.setTranslateX(snake.getSnake().get(0).getTranslateX());
                tempSegment.setTranslateY(snake.getSnake().get(0).getTranslateY() + Main.BLOCK_SIZE); // Move down
                break;
            case LEFT:
                tempSegment.setTranslateX(snake.getSnake().get(0).getTranslateX() - Main.BLOCK_SIZE); // Move left
                tempSegment.setTranslateY(snake.getSnake().get(0).getTranslateY());
                break;
            case RIGHT:
                tempSegment.setTranslateX(snake.getSnake().get(0).getTranslateX() + Main.BLOCK_SIZE); // Move Right
                tempSegment.setTranslateY(snake.getSnake().get(0).getTranslateY());
                break;
        }
    }

    /** GETTER */
    public Directions getDirection() {
        return direction;
    }

    /** SETTER */
    public void setDirection(Directions direction) {
        this.direction = direction;
    }

}