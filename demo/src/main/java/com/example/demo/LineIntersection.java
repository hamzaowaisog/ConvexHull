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

public class LineIntersection extends Application {
    Button b1, b2,b3,b4;
    Text txt;
    @Override
    public void start(Stage stage) throws Exception {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(15, 15, 15, 15));
        vbox.setAlignment(Pos.CENTER);

        txt = new Text("Line Intersection");
        txt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        txt.setFill(Color.DARKBLUE);

        b1 = new Button("Slope Method");
        b1.setFont(Font.font("Arial", FontWeight.BOLD, 14));


        b2 = new Button("Area Method");
        b2.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        b3 = new Button("Sweep Line");
        b3.setFont(Font.font("Arial",FontWeight.BOLD,14));

        b4 = new Button("BACK");
        b4.setFont(Font.font("Arial",FontWeight.BOLD,14));


        vbox.getChildren().addAll(txt, b1, b2, b3, b4);

        Scene scene = new Scene(vbox, 400, 500);

        stage.setScene(scene);
        stage.show();

        b1.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
        Stage slopeMethodStage = new Stage();
        SlopeMethod slopeMethod = new SlopeMethod();
        try {
            slopeMethod.start(slopeMethodStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.close();
    }
});
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage areamethod = new Stage();
                BruteForceLineIntersection brute = new BruteForceLineIntersection();
                try {
                    brute.start(areamethod);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();
            }
        });

        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage sweep = new Stage();
                SweepLineAlgorithm sweepLineAlgorithm = new SweepLineAlgorithm();
                try {
                    sweepLineAlgorithm.start(sweep);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                stage.close();
            }
        });
        b4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage back = new Stage();
                Welcome w = new Welcome();
                w.start(back);
                stage.close();
            }
        });

    }
}
