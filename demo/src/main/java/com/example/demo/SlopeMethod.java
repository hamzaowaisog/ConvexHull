package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class SlopeMethod extends Application {
    points p1,p2,p3,p4;
    Pane pane;
    Canvas canvas;
    GraphicsContext gc;
    Text t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12;
    Button b1;
    long initialtime,finaltime,timecomplexity;
    long initialmemoryusage,finalmemoryusage,memorycomplexity;
    ArrayList<points> intersectionpoints = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        pane = new Pane();
        p1 = new points();
        p2 = new points();
        p3 = new points();
        p4 = new points();
        canvas = new Canvas(650, 500);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        Scene scene = new Scene(pane, 650, 1000);
        final int[] i = {0};
        scene.setOnMouseClicked(mouseEvent -> {
            if(i[0] ==0){
                p1.setX(mouseEvent.getX());
                p1.setY(mouseEvent.getY());
                givedotscordinates(p1,"p1");

            }
            else if(i[0] ==1){
                p2.setX(mouseEvent.getX());
                p2.setY(mouseEvent.getY());
                givedotscordinates(p2,"p2");

            }
            else if(i[0] ==2){
                p3.setX(mouseEvent.getX());
                p3.setY(mouseEvent.getY());
                givedotscordinates(p3,"p3");

            }
            else if(i[0] ==3){
                p4.setX(mouseEvent.getX());
                p4.setY(mouseEvent.getY());
                givedotscordinates(p4,"p4");
            }
            i[0]++;
            if(i[0]==4){
                drawlines(p1,p2,p3,p4);
                printcordinates(p1,p2,p3,p4);
            }



        });



        stage.setTitle("Slope Metohd Line Intersection");
        scene.setFill(Color.LIGHTGRAY);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 650, 500);

        t1 = new Text("Before Execution time");
        t1.setX(10);
        t1.setY(525);
        t3 = new Text("After Execution time");
        t3.setX(10);
        t3.setY(600);
        t5 = new Text("Time complexity");
        t5.setX(10);
        t5.setY(675);
        t7 = new Text("Initial Memory Usage");
        t7.setX(10);
        t7.setY(750);
        t9 = new Text("Final Memory Usage");
        t9.setX(10);
        t9.setY(825);
        t11 = new Text("Memory Complexity");
        t11.setX(10);
        t11.setY(900);

        b1= new Button("Run");
        b1.setLayoutX(300);
        b1.setLayoutY(950);
        b1.setPrefWidth(90);
        b1.setPrefHeight(30);
        b1.setOnAction(actionEvent -> {
            if(p1 == null || p2 == null || p3 == null || p4 == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please enter all the points");
                alert.showAndWait();
            }
            else {
                run(actionEvent);
            }
        });
        Button clearButton = new Button("Clear");
        clearButton.setLayoutX(400);
        clearButton.setLayoutY(950);
        clearButton.setPrefWidth(90);
        clearButton.setPrefHeight(30);
        clearButton.setOnAction(event -> {
            i[0] = 0;

            pane.getChildren().removeIf(node -> node instanceof Circle || node instanceof Text && "dots".equals(node.getId()));
            pane.getChildren().removeIf(node -> node instanceof Line &&("line1".equals(node.getId()) || "line2".equals(node.getId())));
            intersectionpoints.clear();
            pane.getChildren().remove(t2);
            pane.getChildren().remove(t4);
            pane.getChildren().remove(t6);
            pane.getChildren().remove(t8);
            pane.getChildren().remove(t10);
            pane.getChildren().remove(t12);
        });
        Button back = new Button("Back");
        back.setLayoutX(200);
        back.setLayoutY(950);
        back.setPrefWidth(90);
        back.setPrefHeight(30);
        back.setOnAction(actionEvent ->{
            Stage s1 = new Stage();
            LineIntersection hull = new LineIntersection();
            try {
                hull.start(s1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            stage.close();

        });

        pane.getChildren().add(t1);
        pane.getChildren().add(t3);
        pane.getChildren().add(t5);
        pane.getChildren().add(t7);
        pane.getChildren().add(t9);
        pane.getChildren().add(t11);
        pane.getChildren().add(b1);
        pane.getChildren().add(clearButton);
        pane.getChildren().add(back);

        stage.setScene(scene);
        stage.show();

    }

    private void run(ActionEvent actionEvent) {
        initialmemoryusage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        initialtime = System.currentTimeMillis();
        t2 = new Text(String.valueOf(initialtime)+" miliseconds");
        t2.setX(200);
        t2.setY(525);
        pane.getChildren().add(t2);

        t8 = new Text(String.valueOf(initialmemoryusage));
        t8.setX(200);
        t8.setY(750);
        pane.getChildren().add(t8);

        if (doIntersect(p1, p2, p3, p4)) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Slope Line Intersection");
                alert.setHeaderText("Lines Intersect");
                alert.setContentText("Lines Intersect");
                alert.showAndWait();
            });
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Slope Line Intersection");
                alert.setHeaderText("Lines do not Intersect");
                alert.setContentText("Lines do not Intersect");
                alert.showAndWait();
            });
        }
        finaltime = System.currentTimeMillis();
        timecomplexity = finaltime - initialtime;
        t4 = new Text(String.valueOf(finaltime)+" miliseconds");
        t4.setX(200);
        t4.setY(600);
        pane.getChildren().add(t4);

        t6 = new Text(String.valueOf(timecomplexity)+" miliseconds");
        t6.setX(200);
        t6.setY(675);
        pane.getChildren().add(t6);

        finalmemoryusage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        memorycomplexity = finalmemoryusage - initialmemoryusage;
        t10 = new Text(String.valueOf(finalmemoryusage));
        t10.setX(200);
        t10.setY(825);
        pane.getChildren().add(t10);

        t12 = new Text(String.valueOf(memorycomplexity));
        t12.setX(200);
        t12.setY(900);
        pane.getChildren().add(t12);



    }

    public void createdots ( double x, double y, String label){
        Circle dots = new Circle(x, y, 5);
        dots.setFill(javafx.scene.paint.Color.RED);
        dots.setStroke(javafx.scene.paint.Color.BLACK);

        String formattedX = String.format("%.2f", x);
        String formattedY = String.format("%.2f", y);

        Text text = new Text(label + "(" + formattedX + "," + formattedY + ")");
        text.setId("dots");
        text.setX(x - 10);
        text.setY(y - 10);
        pane.getChildren().add(dots);
        pane.getChildren().add(text);

    }
    public void givedotscordinates(points p,String label){
        createdots(p.getX(),p.getY(),label);
    }
    public void printcordinates(points p1 , points p2 , points p3 , points p4){
        System.out.println("p1 : ("+p1.getX()+","+p1.getY()+")");
        System.out.println("p2 : ("+p2.getX()+","+p2.getY()+")");
        System.out.println("p3 : ("+p3.getX()+","+p3.getY()+")");
        System.out.println("p4 : ("+p4.getX()+","+p4.getY()+")");
    }
    public void drawlines(points p1 , points p2 , points p3 , points p4){

        Line line1 = new Line(p1.getX(),p1.getY(),p2.getX(),p2.getY());
        line1.setId("line1");
        Line line2 = new Line(p3.getX(),p3.getY(),p4.getX(),p4.getY());
        line2.setId("line2");
        pane.getChildren().add(line1);
        pane.getChildren().add(line2);
    }

    private boolean doIntersect(points p1,points p2 ,points p3 , points p4){
        PauseTransition pause1 = new PauseTransition(Duration.seconds(4));
        pause1.setOnFinished(event -> {
            try {
                displayOrientationResult(p1, p2, p3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            PauseTransition pause2 = new PauseTransition(Duration.seconds(4));
            pause2.setOnFinished(event2 -> {
                try {
                    displayOrientationResult(p1, p2, p4);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                PauseTransition pause3 = new PauseTransition(Duration.seconds(4));
                pause3.setOnFinished(event3 -> {
                    try {
                        displayOrientationResult(p3, p4, p1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    PauseTransition pause4 = new PauseTransition(Duration.seconds(4));
                    pause4.setOnFinished(event4 -> {
                        try {
                            displayOrientationResult(p3, p4, p2);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    pause4.play();
                });

                pause3.play();
            });

            pause2.play();
        });

        pause1.play();

        if((ccwslope(p1,p2,p3) != ccwslope(p1,p2,p4)) && (ccwslope(p3,p4,p1) != ccwslope(p3,p4,p2))){
            double x = ((p1.getX() * p2.getY() - p1.getY() * p2.getX()) * (p3.getX() - p4.getX())
                    - (p1.getX() - p2.getX()) * (p3.getX() * p4.getY() - p3.getY() * p4.getX()))
                    / ((p1.getX() - p2.getX()) * (p3.getY() - p4.getY()) - (p1.getY() - p2.getY()) * (p3.getX() - p4.getX()));
            double y = ((p1.getX() * p2.getY() - p1.getY() * p2.getX()) * (p3.getY() - p4.getY())
                    - (p1.getY() - p2.getY()) * (p3.getX() * p4.getY() - p3.getY() * p4.getX()))
                    / ((p1.getX() - p2.getX()) * (p3.getY() - p4.getY()) - (p1.getY() - p2.getY()) * (p3.getX() - p4.getX()));
            intersectionpoints.add(new points(x, y));
            colordots();

            return true;
        }
        if(ccwslope(p1,p2,p3) == 0 && onSegment(p1,p3,p2)){
            return true;
        }
        if(ccwslope(p1,p2,p4) == 0 && onSegment(p1,p4,p2)){
            return true;
        }
        if(ccwslope(p3,p4,p1) == 0 && onSegment(p3,p1,p4)){
            return true;
        }
        if(ccwslope(p3,p4,p2) == 0 && onSegment(p3,p2,p4)){
            return true;
        }
        return false;

    }
    public boolean onSegment (points p , points q , points r){
        if(q.getX()<= Math.max(p.getX(),r.getX()) && q.getX()>= Math.min(p.getX(),r.getX()) && q.getY()<= Math.max(p.getY(),r.getY()) && q.getY()>= Math.min(p.getY(),r.getY())){
            return true;
        }
        return false;
    }
    private double ccwslope (points p1,points p2, points p3){
        double val = (p2.getY()-p1.getY())*(p3.getX()-p2.getX()) - (p2.getX()-p1.getX())*(p3.getY()-p2.getY());
       if(val == 0){
                return 0;
            }
            else if(val>0){
                return 1; //clockwise
            }
            else {
                return -1; //anti clockwise
            }
    }
    public void colordots(){
        for (points intersection : intersectionpoints) {
            givedotscordinates(intersection,"I");
            Circle intersectionDot = new Circle(intersection.getX(), intersection.getY(), 5);
            intersectionDot.setFill(javafx.scene.paint.Color.GREEN);
            intersectionDot.setStroke(javafx.scene.paint.Color.BLACK);
            pane.getChildren().add(intersectionDot);
        }
    }

    private void displayOrientationResult(points p , points q , points r) throws InterruptedException {
        Circle dot1 = new Circle(p.getX(), p.getY(), 5);
        dot1.setFill(javafx.scene.paint.Color.BLUE);
        dot1.setStroke(javafx.scene.paint.Color.BLACK);

        Circle dot2 = new Circle(q.getX(), q.getY(), 5);
        dot2.setFill(javafx.scene.paint.Color.BLUE);
        dot2.setStroke(javafx.scene.paint.Color.BLACK);

        Circle dot3 = new Circle(r.getX(), r.getY(), 5);
        dot3.setFill(Color.YELLOW);
        dot3.setStroke(javafx.scene.paint.Color.BLACK);

        double orientation = ccwslope(p, q, r);
        System.out.println(orientation);
        pane.getChildren().add(dot1);
        pane.getChildren().add(dot2);
        pane.getChildren().add(dot3);

        String result;
        double deltaX = r.getX() - q.getX();
        double deltaY = r.getY() - q.getY();
        double arrowLength = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        Arrow arrow = new Arrow(q.getX(), q.getY(), arrowLength, orientation > 0, r.getX(), r.getY());
        pane.getChildren().add(arrow);
        Label label = new Label(orientation > 0 ? "CW" : "ACW");
        label.setLayoutX(q.getX());
        label.setLayoutY(q.getY() + 20);
        pane.getChildren().add(label);

        if (orientation == 0) {
            result = "Collinear";

        } else if (orientation > 0) {
            result = "AntiClockwise";
        } else {
            result = "Clockwise";
        }
        System.out.println("Orientation of (" + p.getX() + "," + p.getY() + ") , (" + q.getX() + "," + q.getY() + ") , (" + r.getX() + "," + r.getY() + ") is " + result);
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(event -> {
            pane.getChildren().remove(dot1);
            pane.getChildren().remove(dot2);
            pane.getChildren().remove(dot3);
            pane.getChildren().remove(arrow);
            pane.getChildren().remove(label);
        });
        pause.play();

    }
}



