/**
 * InputBox class
 * Displays a small window the asks the player for their initials, updates
 * and displays the high scores
 * References
 * https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength
 * https://stackoverflow.com/questions/2874821/capitalize-all-letters-in-a-textfield-in-java
 * https://www.youtube.com/watch?v=cwJK_WpseKQ
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

public class InputBox {
    /** Fields
     * username stores a String username
     * data stores a Pair of a String array of the
     * top scoring player names and an int array of their scores*/
    private String username = "AAA";
    private Pair<String[], int[]> data;

    /**
     * Public display method
     * shows a small window with a TextField and prompts the player to enter their
     * initials updates and displays the high scores using the DBManager class to
     * insert and retrieve the high scores and creates a HighScoreBox on exit to
     * display the scores. An instance of AlertBox is used to validate input.
     * */
    public String display(String title, String message, int score) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label lblMessage = new Label();
        lblMessage.setText(message);

        TextField tfInput = new TextField("AAA");

        // Constrain input to three characters (Max)
        tfInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (tfInput.getText().length() > 3) {
                    tfInput.setText(tfInput.getText().substring(0, 3));
                }
            }
        });

        // Constrain input to uppercase letters
        tfInput.setTextFormatter(new TextFormatter<Object>(e -> {
            e.setText(e.getText().toUpperCase());
            return e;
        }));

        // Exit and show high scores
        Button btOk = new Button("Ok");
        btOk.setOnAction(e -> {
            if (isValidInput(tfInput, tfInput.getText())) {
                username = tfInput.getText();
                window.close();
                DBManager.insertHighScore(tfInput.getText(), score);
                data = DBManager.getScores();
                new HighScoreBox().display(data.getKey(), data.getValue());
            }
        });

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(lblMessage, tfInput, btOk);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
        return username;
    }

    /** Private isValidInput
     * returns true if the TextField contains only letters and exactly three initials
     * otherwise resets the TextField, shows an error message in a window and returns
     * false*/
    private boolean isValidInput(TextField tfInput, String text) {
        if (text.matches("[a-zA-Z]+") && text.length() == 3) return true;
        else {
            new AlertBox().display("Error", "Please Enter Three Letters Only");
            tfInput.setText("AAA");
            tfInput.requestFocus();
            return false;
        }
    }
}

