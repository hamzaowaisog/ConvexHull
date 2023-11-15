package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


import java.util.ArrayList;

public class SweepLineAlgorithm extends Application {
    points p1,p2,p3,p4;
    Pane pane;
    Canvas canvas;
    GraphicsContext gc;
    Text t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12;
    Button b1;
    long initialtime,finaltime,timecomplexity;
    long initialmemoryusage,finalmemoryusage,memorycomplexity;
    ArrayList<points> intersectionpoints = new ArrayList<>();

    Line sweepLine;
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



        stage.setTitle("Sweep Line Intersection");
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
            pane.getChildren().remove(sweepLine);
        });

        pane.getChildren().add(t1);
        pane.getChildren().add(t3);
        pane.getChildren().add(t5);
        pane.getChildren().add(t7);
        pane.getChildren().add(t9);
        pane.getChildren().add(t11);
        pane.getChildren().add(b1);
        pane.getChildren().add(clearButton);

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

        boolean find = doIntersect(p1,p2,p3,p4);

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

    public boolean doIntersect(points p1, points p2, points p3, points p4) {
        LineSegment segment1 = new LineSegment(p1, p2);
        LineSegment segment2 = new LineSegment(p3, p4);

        double maxX = canvas.getWidth();
        double maxY = canvas.getHeight();
        sweepLine = new Line(0, 0, maxX, 0);
        sweepLine.setStroke(Color.BLUE);
        pane.getChildren().add(sweepLine);

        // Initialize the Timeline
        Timeline timeline = new Timeline();

        // Set up the KeyFrame for animated movement
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
            // Move the sweep line downwards
            double yIncrement = 1; // You can adjust the increment based on your needs
            sweepLine.setStartY(sweepLine.getStartY() + yIncrement);
            sweepLine.setEndY(sweepLine.getEndY() + yIncrement);

            // Check if the sweep line touches one of the end points
            if (sweepLine.getStartY() == segment1.getStart().getY() || sweepLine.getStartY() == segment2.getStart().getY()) {
                // Check if the line segments intersect
                if (segment1.intersects(segment2)) {
                    // The lines intersect
                    points intersection = segment1.getIntersection(segment2);

                    // Draw the intersection point
                    givedotscordinates(intersection,"I");
                    Circle intersectionDot = new Circle(intersection.getX(), intersection.getY(), 5);
                    intersectionDot.setFill(Color.GREEN);
                    intersectionDot.setStroke(Color.BLACK);
                    pane.getChildren().add(intersectionDot);
                }
            }
        });

        // Add the KeyFrame to the Timeline
        timeline.getKeyFrames().add(keyFrame);

        // Set up the Timeline to repeat until maxY is reached
        timeline.setCycleCount((int) (maxY));

        // Set up an event to show the alert after the timeline completes
        timeline.setOnFinished(event -> {
            // Check if the line segments intersect
            if (segment1.intersects(segment2)) {
                // The lines intersect
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sweep Line Intersection");
                    alert.setHeaderText("Lines Intersect");
                    alert.setContentText("Lines Intersect");
                    alert.showAndWait();
                });
            } else {
                // The lines do not intersect
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sweep Line Intersection");
                    alert.setHeaderText("Lines do not Intersect");
                    alert.setContentText("Lines do not Intersect");
                    alert.showAndWait();
                });
            }
        });

        // Start the Timeline
        timeline.play();

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



        return false;
    }



}
class LineSegment {
    private points start;
    private points end;

    public LineSegment(points start, points end) {
        this.start = start;
        this.end = end;
    }

    public points getStart() {
        return start;
    }

    public points getEnd() {
        return end;
    }

    public boolean contains(points p) {
        return p.equals(start) || p.equals(end);
    }

    public boolean intersects(LineSegment other) {
        // Use the cross product to check if two line segments intersect
        double d1 = direction(other.getStart(), other.getEnd(), this.start);
        double d2 = direction(other.getStart(), other.getEnd(), this.end);
        double d3 = direction(this.start, this.end, other.getStart());
        double d4 = direction(this.start, this.end, other.getEnd());

        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            return true;
        } else if (d1 == 0 && onSegment(other.getStart(), other.getEnd(), this.start)) {
            return true;
        } else if (d2 == 0 && onSegment(other.getStart(), other.getEnd(), this.end)) {
            return true;
        } else if (d3 == 0 && onSegment(this.start, this.end, other.getStart())) {
            return true;
        } else if (d4 == 0 && onSegment(this.start, this.end, other.getEnd())) {
            return true;
        }

        return false;
    }
    public points getIntersection(LineSegment other) {
        double x1 = this.start.getX();
        double y1 = this.start.getY();
        double x2 = this.end.getX();
        double y2 = this.end.getY();
        double x3 = other.getStart().getX();
        double y3 = other.getStart().getY();
        double x4 = other.getEnd().getX();
        double y4 = other.getEnd().getY();

        double denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));

        if (denominator == 0) {
            if (onSegment(this.start, this.end, other.getStart())) {
                return other.getStart();
            } else if (onSegment(this.start, this.end, other.getEnd())) {
                return other.getEnd();
            } else {
                return null;
            }
        }

        double x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denominator;
        double y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denominator;

        return new points(x, y);
    }


    private double direction(points pi, points pj, points pk) {
        return (pk.getX() - pi.getX()) * (pj.getY() - pi.getY()) - (pj.getX() - pi.getX()) * (pk.getY() - pi.getY());
    }

    private boolean onSegment(points pi, points pj, points pk) {
        if ((Math.min(pi.getX(), pj.getX()) <= pk.getX() && pk.getX() <= Math.max(pi.getX(), pj.getX()))
                && (Math.min(pi.getY(), pj.getY()) <= pk.getY() && pk.getY() <= Math.max(pi.getY(), pj.getY()))) {
            return true;
        }
        return false;
    }
}
