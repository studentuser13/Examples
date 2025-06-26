import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/** Food class
 * Creates a food sprite and gives it a game position */

public class Food{
    /** Fields
     * imageFood stores an image of the snake's food
     * ivFood stores an ImageView of imageFood
     * xPos stores an int xPos position
     * yPos stores an int yPos position*/
    private Image imageFood =  new Image("res/apple.png");
    private ImageView foodView;
    private int xPos, yPos;

    /** Constructor
     * initializes the ivFood and the food's game position */
    public Food(Snake snake) {
        this.foodView = new ImageView(imageFood);
        randomMove(snake);
    }

    /** randomMove method
     * Finds a new game position for the food and calls isOverlap to check
     * its position against the snake's*/
    public void randomMove(Snake snake) {
        this.xPos = ((int)(Math.random()*(Main.APP_W-Main.BLOCK_SIZE))/Main.BLOCK_SIZE * Main.BLOCK_SIZE);
        this.yPos = ((int)(Math.random()*(Main.APP_H-Main.BLOCK_SIZE))/Main.BLOCK_SIZE * Main.BLOCK_SIZE);
        if (this.yPos == 0) this.yPos = yPos +40;
        getFoodView().setFitHeight(40);
        getFoodView().setPreserveRatio(true);
        getFoodView().setTranslateX(xPos);
        getFoodView().setTranslateY(yPos);
        if (this.yPos == 0) this.yPos = yPos +40;
        getFoodView().setFitHeight(40);
        getFoodView().setPreserveRatio(true);
        getFoodView().setTranslateX(xPos);
        getFoodView().setTranslateY(yPos);
        if (isOverlap(snake)) {
            randomMove(snake);
        }
    }

    /** isOverlap method
     * checks the food's position in the game against the snake's
     * for overlaps, if so it returns true */
    public boolean isOverlap(Snake snake) {
        for (Node segment : snake.getSnake()) {
            if (foodView.getTranslateX() == segment.getTranslateX()
                    && foodView.getTranslateY() == segment.getTranslateY()) {
                return true;
            }
        }
        return false;
    }

    /** GETTER */
    public ImageView getFoodView() {
        return foodView;
    }
}