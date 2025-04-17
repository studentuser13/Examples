import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
/** StartScreen class
 * The start screen of the game */
/********************************************
//            INTRO SCENE LAYOUT           //
//******************************************/
public class StartScreen {
    /** Fields
     * root stores the Pane start screen */
    private Pane root;

    public StartScreen(){
        this.root = createLayout();
    }

    /** Getter */
    public Pane getRoot(){
        return root;
    }

    /** Private createLayout method
     * Builds and returns the game's start screen */
    private Pane createLayout() {
        root = new Pane();
        root.setPrefSize(Main.APP_W, Main.APP_H);

        // Logo
        ImageView logoView = new ImageView(new Image("res/snake.png"));
        logoView.setFitHeight(500);
        logoView.setPreserveRatio(true);
        logoView.setLayoutX(40);
        logoView.setLayoutY(40);

        // Controls diagram
        ImageView controlsView = new ImageView(new Image("res/controls.png"));
        controlsView.setFitHeight(100);
        controlsView.setPreserveRatio(true);
        controlsView.setLayoutX(350);
        controlsView.setLayoutY(40);

        // Control instructions
        Text keys = new Text("Use arrow keys to direct the snake \nspacebar to pause / resume the game");
        keys.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        keys.setFill(Color.BLUE);
        keys.setLayoutX(470);
        keys.setLayoutY(90);

        // Play button image
        ImageView playView = new ImageView(new Image("res/play.png"));
        playView.setFitHeight(150);
        playView.setPreserveRatio(true);
        playView.setLayoutX(320);
        playView.setLayoutY(170);

        // Click here instructions
        // Control instructions
        Text clickHere = new Text("Click to play");
        clickHere.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        clickHere.setFill(Color.BLUE);
        clickHere.setLayoutX(348);
        clickHere.setLayoutY(348);

        // Background
        ImageView backgroundView = new ImageView(Main.BACKGROUND_IMAGE);

        // Add elements to the root
        root.getChildren().setAll(backgroundView, logoView, controlsView, keys, playView, clickHere);

        return root;
    }
}
