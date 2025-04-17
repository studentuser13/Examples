//original idea by Almas Baimagambetov was completely redesigned by COIS 2240 GROUP 7
//https://www.youtube.com/watch?v=nK6l1uVlunc
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.AudioClip;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
/** Main class
 * Runs a Snake game using JavaFX using instances of Snake and Food classes for game
 * objects and an instance of Directions class for gameplay logic. High scores are
 * tracked and displayed in a SQLite database by calling DBManager, HighScoreBox and
 * InputBox classes. An introductory scene with instructions for the player using an
 * instance of StartScreen class */
public class Main extends Application {
    /** Fields
     * Constants */
    // Board
    public static final int BLOCK_SIZE = 40;
    public static final int APP_W = 20 * BLOCK_SIZE;
    public static final int APP_H = 15 * BLOCK_SIZE;
    // Background image
    public final static Image BACKGROUND_IMAGE = new Image("res/BG.png");
    /** Variables */
    // Game control variables
    private int gameLevel = 1;
    private int segmentsToAdd = 0;
    private int score = 0;
    private int pause = 0;
    private boolean isMoved = false;
    private boolean isRunning = false;
    private boolean isSoundOn = true;
    private boolean isGameOver = false;
    // Segment position variables
    private double X0;
    private double Y0;
    private double X1;
    private double Y1;
    private Direction direction = new Direction();
    private char dir = 'R';
    // Nodes
    private Snake snake;
    private Food food;
    private Node tempSegment;
    private Text text;
    private ImageView headView, neckView, segmentView;
    // Scene related
    private Scene intro, game;
    private Timeline timeline = new Timeline();
    // Database
    private Pair<String[],int[]> data;

    /************************************************
     //  the main function that launches JavaFx     //
     //**********************************************/
    public static void main(String[] args) {
        launch(args);
    }

    /************************************************
     //               the START method              //
     //**********************************************/
    @Override
    public void start(Stage primaryStage) throws Exception {
        game = new Scene(getSnakeGame());
        intro = new Scene(new StartScreen().getRoot());
        // Enter game
        intro.setOnMouseClicked(event -> {
            primaryStage.setScene(game);
            primaryStage.show();
            isRunning = true;
            timeline.play();
        });
        // Handle game events
        game.setOnKeyPressed(event -> {
            if (!isMoved && pause == 0)
                return;

            switch (event.getCode()) {
                case UP:
                    if (direction.getDirection() != Direction.Directions.DOWN && pause != 1) {
                        direction.setDirection(Direction.Directions.UP);
                        playSound('U');
                        dir = 'U';
                        headView = Segment.changeSegmentView(dir);
                        headView.setTranslateX(X0);
                        headView.setTranslateY(Y0);
                        snake.getSnake().set(0, headView);
                    }
                    break;
                case DOWN:
                    if (direction.getDirection() != Direction.Directions.UP && pause != 1) {
                        direction.setDirection(Direction.Directions.DOWN);
                        playSound('D');
                        dir = 'D';
                        headView = Segment.changeSegmentView(dir);
                        headView.setTranslateX(X0);
                        headView.setTranslateY(Y0);
                        snake.getSnake().set(0, headView);
                    }
                    break;
                case LEFT:
                    if (direction.getDirection() != Direction.Directions.RIGHT && pause != 1) {
                        direction.setDirection(Direction.Directions.LEFT);
                        playSound('L');
                        dir = 'L';
                        headView = Segment.changeSegmentView(dir);
                        headView.setTranslateX(X0);
                        headView.setTranslateY(Y0);
                        snake.getSnake().set(0, headView);
                    }
                    break;
                case RIGHT:
                    if (direction.getDirection() != Direction.Directions.LEFT && pause != 1) {
                        direction.setDirection(Direction.Directions.RIGHT);
                        playSound('R');
                        dir = 'R';
                        headView = Segment.changeSegmentView(dir);
                        headView.setTranslateX(X0);
                        headView.setTranslateY(Y0);
                        snake.getSnake().set(0, headView);
                    }
                    break;
                case SPACE:
                    if (pause == 0) {
                        pauseGame(snake);
                    }
                    else {
                        resumeGame(snake);
                    }
                    break;
                default:
                    break;
            }

            isMoved = false;
        });
        // Initialize primaryStage
        primaryStage.setTitle("SNAKE GAME");
        primaryStage.setResizable(false);
        primaryStage.setMaxWidth(APP_W+6);
        primaryStage.setMaxHeight(APP_H+36);

        primaryStage.setScene(intro);
        primaryStage.show();

        startGame(snake);

        /********* LISTENER TO CATCH CHANGES IN PRIMARYSTAGE PROPERTIES *******/
        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {

                if (isGameOver == true) {
                    isRunning = false;
                    primaryStage.setScene(intro);
                    primaryStage.show();
                    //Clear GameOver message from the top bar
                    text.setText("Level: " + gameLevel + "   Score: " + score);
                    food.randomMove(snake);
                    isGameOver = false;
                }
            }
        });
        // ---------- end of LISTENER ------------ //
    }
    // ---------- end of Start ------------ //

    /******************************************
    //            GAME SCENE LAYOUT          //
    //****************************************/
    /** Private getSnakeGame
     * returns the Pane of the game's layout and initializes the game animation */
    private Pane getSnakeGame() {
        // root Pane
        Pane root = new Pane();
        root.setPrefSize(APP_W, APP_H);
        // Background
        final ImageView backgroundView = new ImageView(BACKGROUND_IMAGE);
        backgroundView.setLayoutY(40);

        // ************* text showing score and game level *********** //
        text = new Text("Level: " + gameLevel + "   Score: " + score);
        text.setFont(Font.font("Calibri", FontWeight.BOLD, 30));
        text.setFill(Color.WHITE);
        text.setLayoutX(30);
        text.setLayoutY(30);

        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.5f, 0.5f, 0.5f));

        text.setEffect(ds);
        // --- end of text showing score and game level --- //

        // ************ MUTE *************** //
        Image soundOn =  new Image("res/sound_on.png");
        Image soundOff =  new Image("res/sound_off.png");
        ImageView soundView = new ImageView(soundOn);
        soundView.setFitHeight(30);
        soundView.setPreserveRatio(true);
        soundView.setLayoutX(760);
        soundView.setLayoutY(5);
        soundView.setOnMouseClicked(event -> {
            if(isSoundOn == true) {
                isSoundOn = false;
                soundView.setImage(soundOff);
            }else {
                isSoundOn = true;
                soundView.setImage(soundOn);
            }
        });
        //------------ end of MUTE ------------ //

        /** Initialize game sprites */
        Group snakeBody = new Group();
        snake = new Snake(snakeBody);
        food = new Food(snake);


        // ******************** KEYFRAME *********************//
        KeyFrame frame = new KeyFrame(Duration.seconds(0.3), event -> {
            if(!isRunning)
                return;

            if (snake.getSnake().size() <= 1) {
                tempSegment = snake.getSnake().get(0);
            }

            X1 = tempSegment.getTranslateX();
            Y1 = tempSegment.getTranslateY();
            // Position the tempSegment in front of the current head
            Direction.moveInDirection(direction.getDirection(), snake, tempSegment);

            X0 = tempSegment.getTranslateX();
            Y0 = tempSegment.getTranslateY();

            isMoved = true;

            if (snake.getSnake().size() == 1) {
                if (segmentsToAdd == 0) {
                    playSound('M');
                    snake.getSnake().get(0).setTranslateX(X0);
                    snake.getSnake().get(0).setTranslateY(Y0);
                } else {
                    playSound('M');
                    headView = Segment.changeSegmentView(dir);
                    headView.setTranslateX(X0);
                    headView.setTranslateY(Y0);
                    snake.getSnake().add(0, headView);

                    neckView = Segment.changeSegmentView('S');
                    neckView.setTranslateX(X1);
                    neckView.setTranslateY(Y1);
                    snake.getSnake().set(1, neckView);
                    segmentsToAdd --;
                }
            } else if (snake.getSnake().size() > 1) {
                if (segmentsToAdd == 0) {
                    playSound('M');
                    headView = Segment.changeSegmentView(dir);
                    headView.setTranslateX(X0);
                    headView.setTranslateY(Y0);

                    neckView = Segment.changeSegmentView('S');
                    neckView.setTranslateX(X1);
                    neckView.setTranslateY(Y1);
                    snake.getSnake().add(0, headView);
                    snake.getSnake().set(1, neckView);
                    snake.getSnake().remove(snake.getSnake().size()-1);
                } else {
                    playSound('M');
                    ImageView headView = Segment.changeSegmentView(dir);
                    headView.setTranslateX(X0);
                    headView.setTranslateY(Y0);
                    neckView = Segment.changeSegmentView('S');
                    neckView.setTranslateX(X1);
                    neckView.setTranslateY(Y1);
                    snake.getSnake().add(0, headView);
                    snake.getSnake().set(1, neckView);
                    segmentsToAdd --;
                }
            }
            // Update the score
            text.setText("Level: " + gameLevel + "   Score: " + score);

            // ******************** COLLISION DETECTION ********************** //
            // If the head of the snake collides with any of its segments then restart the game
            for (Node segment : snake.getSnake()) {
                if (segment != snake.getSnake().get(0) && X0 == segment.getTranslateX()
                        && Y0 == segment.getTranslateY()) {
                    playSound('C');
                    playSound('E');
                    stopGame(snake);
                    break;
                }
            }
            // If the head of the snake moves outside the board bounds then restart the game
            if (X0 < 0 || X0 >= APP_W || Y0 < 40 || Y0 >= APP_H) {
                playSound('C');
                playSound('E');
                stopGame(snake);
            }
            // ---------------- End of COLLISION DETECTION ----------------- //

            // ************ snake eating food scenario ***************** //
            if (food.getFoodView().getTranslateX() == X0 && food.getFoodView().getTranslateY() == Y0) {
                food.randomMove(snake);
                score ++;
                segmentsToAdd += gameLevel;
                playSound('B');
            }
            // Update game level (and amount of segments to add consequentially) based on score
            if (score > 40 && gameLevel != 5) {
                gameLevel = 5;
            } if (score <= 40 && gameLevel != 4) {
                gameLevel = 4;
            } if (score <= 30 && gameLevel != 3) {
                gameLevel = 3;
            } if (score <= 20 && gameLevel != 2) {
                gameLevel = 2;
            } if (score <= 10 && gameLevel != 1) {
                gameLevel = 1;
            }
        });
        // ----------- end of KEYFRAME ----------------- //

        // Update the Timeline
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        // Scores and message bar
        Pane p = new Pane();
        p.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        p.getChildren().addAll(backgroundView,text,soundView);

        root.getChildren().addAll(p,food.getFoodView(),snakeBody);

        return root;
    }

    /********************************************************
    //    functions that control start/end of the game     //
    //******************************************************/
    /** Private startGame method
     * Resets the game state to begin play */
    private void startGame(Snake snake) {
        direction.setDirection(Direction.Directions.RIGHT);
        dir = 'R';
        segmentView = Segment.changeSegmentView(dir);
        segmentView.setTranslateX(0);
        segmentView.setTranslateY(280);
        snake.getSnake().add(segmentView);
        gameLevel = 1;
        segmentsToAdd = 0;
        score = 0;
        timeline.play();
    }

    /** Private stopGame method
     * Stops the game and checks the score by calling private checkScore */
    private void stopGame(Snake snake) {
        isGameOver = true;
        snake.getSnake().clear();
        timeline.stop();
        text.setText("                                           GAME OVER");
        checkScore(score);
        startGame(snake);
        isRunning = false;
        timeline.pause();
        pause = 0;
        X0 += 40; //reset snake pos//
    }

    /** Private pauseGame method
     * Pauses the current game */
    private void pauseGame(Snake snake) {
        pause = 1;
        isRunning = false;
        text.setText("                                             PAUSE");
        timeline.pause();
    }

    /** Private resumeGame method
     * Resumes a paused game */
    private void resumeGame(Snake snake) {
        pause = 0;
        isRunning = true;
        text.setText("Level: " + gameLevel + "   Score: " + score);
        timeline.play();

    }

    /** Private checkScore method
     * Checks a given score against the scores database using DBManager's getScores method
     * and gets the player's name if it is a high score using an InputBox */
    private void checkScore(int score){
        boolean isHighScore = false;
        data = DBManager.getScores();
        for(int i = 0; i< 5; i++)
            if (score > data.getValue()[i])
                isHighScore = true;
        if (isHighScore){
            new InputBox().display("Enter Name", "Enter Initials", score);
        }
        else
            new HighScoreBox().display(data.getKey(),data.getValue());
    }

    // **************** SOUND EFFECTS ****************** //
    /** Private playSound method
     * Plays appropriate sounds according to a given char of the game state, or none if mute is selected */
    private void playSound(char dir) {
        if (isSoundOn) {
            switch (dir) {
                case 'U':
                    AudioClip u = new AudioClip(this.getClass().getResource("res/up.wav").toString());
                    u.play();
                    break;
                case 'D':
                    AudioClip d = new AudioClip(this.getClass().getResource("res/down.wav").toString());
                    d.play();
                    break;
                case 'L':
                    AudioClip l = new AudioClip(this.getClass().getResource("res/left.wav").toString());
                    l.play();
                    break;
                case 'R':
                    AudioClip r = new AudioClip(this.getClass().getResource("res/right.wav").toString());
                    r.play();
                    break;
                case 'B':
                    AudioClip b = new AudioClip(this.getClass().getResource("res/bite.wav").toString());
                    b.play();
                    break;
                case 'E':
                    AudioClip e = new AudioClip(this.getClass().getResource("res/ending.wav").toString());
                    e.play();
                    break;
                case 'C':
                    AudioClip c = new AudioClip(this.getClass().getResource("res/crash.wav").toString());
                    c.play();
                    break;
                case 'M':
                    AudioClip m = new AudioClip(this.getClass().getResource("res/move.wav").toString());
                    m.play();
                    break;
                case 'W':
                    AudioClip w = new AudioClip(this.getClass().getResource("res/win.wav").toString());
                    w.play();
                    break;
                default:
                    break;
            }
        }
    }
    // --------- end of SOUND EFFECTS ---------- //
}