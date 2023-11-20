package com.example.demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Welcome extends Application {
    Button b1, b2;
    Text txt;

    @Override
    public void start(Stage stage) {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(15, 15, 15, 15));
        vbox.setAlignment(Pos.CENTER);

        txt = new Text("Welcome to the Application!");
        txt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        txt.setFill(Color.DARKBLUE);

        b1 = new Button("Line Intersection");
        b1.setFont(Font.font("Arial", FontWeight.BOLD, 14));


        b2 = new Button("Convex Hull");
        b2.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        vbox.getChildren().addAll(txt, b1, b2);

        Scene scene = new Scene(vbox, 400, 500);

        stage.setScene(scene);
        stage.show();
        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s1 = new Stage();
                LineIntersection l = new LineIntersection();
                try {
                    l.start(s1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();

            }
        });
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s1 = new Stage();
                ConvexHull hull = new ConvexHull();
                try {
                    hull.start(s1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();

            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}