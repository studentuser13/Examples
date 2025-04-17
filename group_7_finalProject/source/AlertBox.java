/** AlertBox class
 * Displays an simple window with a message and a close button
 * Reference
 * https://www.youtube.com/watch?v=SpL3EToqaXA&t=318s*/
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    /** public display
     * shows a small window with a given message, title and a close button */
    public static void display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label lblMessage = new Label();
        lblMessage.setText(message);

        Button btClose = new Button("Ok");
        btClose.setOnAction(e->window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(lblMessage,btClose);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }
}
