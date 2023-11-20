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

public class ConvexHull extends Application {
    Button b1, b2, b3, b4 ,b5 ,b6;
    Text txt;

    @Override
    public void start(Stage stage) throws Exception {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(15, 15, 15, 15));
        vbox.setAlignment(Pos.CENTER);

        txt = new Text("Convex Hull");
        txt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        txt.setFill(Color.DARKBLUE);

        b1 = new Button("Brute Force");
        b1.setFont(Font.font("Arial", FontWeight.BOLD, 14));


        b2 = new Button("Jarvis March");
        b2.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        b3 = new Button("Graham Scan");
        b3.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        b4 = new Button("Quick Eliminatation");
        b4.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        b5 = new Button("Quick Hull");
        b5.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        b6 = new Button("BACK");
        b6.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        vbox.getChildren().addAll(txt, b1, b2, b3, b4,b5,b6);

        Scene scene = new Scene(vbox, 400, 500);

        stage.setScene(scene);
        stage.show();

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s1 = new Stage();
                BruteForceConvexHull hull = new BruteForceConvexHull();
                try {
                    hull.start(s1);
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
                JarvisMarchConvexHull hull = new JarvisMarchConvexHull();
                try {
                    hull.start(s1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();
            }
        });
        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s1 = new Stage();
                GrahamScanConvexHull hull = new GrahamScanConvexHull();
                try {
                    hull.start(s1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();
            }
        });
        b4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s1 = new Stage();
                QuickEliminationConvexHull hull = new QuickEliminationConvexHull();
                try {
                    hull.start(s1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();
            }
        });
        b5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s1 = new Stage();
                QuickHullConvexHull hull = new QuickHullConvexHull();
                try {
                    hull.start(s1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();
            }
        });
        b6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s1 = new Stage();
                Welcome hull = new Welcome();
                try {
                    hull.start(s1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();
            }
        });

    }
}