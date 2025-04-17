import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
/** Snake class
 * Represents the game's snake logically as a list of observable Nodes */
public class Snake {
    /** Fields
     * snake stores and ObservableList of nodes to store ImageViews
     * of the snake and their positions */
    private ObservableList<Node> snake;

    /** Constructor */
    public Snake(Group snakeBody) {
        this.snake = snakeBody.getChildren(); // Initialize list of nodes
    }

    /** Getter */
    public ObservableList<Node> getSnake() {
        return snake;
    }
}