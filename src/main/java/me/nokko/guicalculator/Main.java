package me.nokko.guicalculator;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static me.nokko.guicalculator.ResultField.Operation.Type.ADD;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calculator");
        primaryStage.show();

        VBox root = new VBox();
        root.setSpacing(10d);
        root.setPadding(new Insets(20, 20, 20, 20));

        final var resultField = new ResultField();

        GridPane numberButtons = createNumberButtons(resultField);

        root.getChildren().add(resultField);

        final Separator separator = new Separator(Orientation.HORIZONTAL);
        root.getChildren().add(separator);
        root.getChildren().add(numberButtons);

        Scene startScene = new Scene(root, 300, 275);


        resultField.addEventFilter(KeyEvent.ANY, (Event::consume));
        resultField.addEventFilter(KeyEvent.KEY_PRESSED, event -> handleKeyPress(event, resultField));

        startScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> handleKeyPress(event, resultField));

        primaryStage.setScene(startScene);
    }

    private void handleKeyPress(KeyEvent event, ResultField field) {
        if (!event.isShortcutDown()) {
            if (event.getCode().isDigitKey() || event.getCode().isKeypadKey()) {
                field.pushCharacter(event.getText());
            }

            if (event.getCode().equals(KeyCode.PERIOD) || event.getCode().equals(KeyCode.SEPARATOR)) {
                field.pushCharacter(".");
            }

            switch (event.getCode()) {
                case BACK_SPACE -> field.backspace();
                case DELETE -> field.pop();
                case ENTER -> field.push();
                case EQUALS,ADD -> {
                    System.out.println("plus?");
                    field.setOp(ADD);
                    field.applyOp();
                }
                case MINUS -> {
                    System.out.println("plus?");
                    field.setOp(ADD);
                    field.applyOp();
                }
                default -> System.out.println(event.getCode().name());
            }
        }
    }

    private GridPane createNumberButtons(ResultField field) {
        GridPane numberButtons = new GridPane();

        numberButtons.setAlignment(Pos.CENTER);
        numberButtons.setHgap(10d);
        numberButtons.setVgap(10d);
        numberButtons.setPadding(new Insets(0, 10, 10, 10));

        // The order we add the elements matters for Tab-key navigation
        // So we start from 7 and go 7,8,9, … 1,2,3 in groups of 3
        for (int i = 2; i > -1; i--) {
            for (int j = 0; j < 3; j++) {

                // the number on the button
                int number = i*3 + j + 1; // off-by-one

                Button button =  new Button("%d".formatted(number));
                button.setOnAction((event -> {
                    field.pushCharacter(String.valueOf(number));
                }));

                numberButtons.add(
                        button
                        , j, 3 - i); // 3 - i because calculators go 7 8 9 … 1 2 3
            }
        }

        return numberButtons;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
