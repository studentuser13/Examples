import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/** Segment class
 * Manages sprites*/
public class Segment {

    private static Image headUp =  new Image("res/SnakeHead_U.png");
    private static Image headDown =  new Image("res/SnakeHead_D.png");
    private static Image headLeft =  new Image("res/SnakeHead_L.png");
    private static Image headRight =  new Image("res/SnakeHead_R.png");
    private static Image segment =  new Image("res/segment+.png");
    private static ImageView segmentView = new ImageView(headRight);

    /** Public changeSegmentView method
     * Returns the ImageView of the Snake's body for the Snake's direction given as a char */
    public static ImageView changeSegmentView(char dir) {
        switch (dir) {
            case 'U':
                segmentView = new ImageView(headUp);
                return segmentView;
            case 'D':
                segmentView = new ImageView(headDown);
                return segmentView;
            case 'L':
                segmentView = new ImageView(headLeft);
                return segmentView;
            case 'R':
                segmentView = new ImageView(headRight);
                return segmentView;
            case 'S':
                segmentView = new ImageView(segment);
                return segmentView;
            default:
                return null;
        }
    }
}