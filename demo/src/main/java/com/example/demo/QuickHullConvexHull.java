package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;

public class QuickHullConvexHull extends Application {
    ArrayList<points> points;
    ArrayList<Line> lines;
    Pane pane;
    Canvas canvas;
    GraphicsContext gc;
    Text t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12;
    Button b1;
    long initialtime, finaltime, timecomplexity;
    long initialmemoryusage, finalmemoryusage, memorycomplexity;
    @Override
    public void start(Stage stage) throws Exception {
        lines = new ArrayList<>();
        pane = new Pane();
        canvas = new Canvas(650, 500);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        Scene scene = new Scene(pane, 650, 1000);

        stage.setTitle("Graham Scan Convex Hull");
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

        b1 = new Button("Run");
        b1.setLayoutX(300);
        b1.setLayoutY(950);
        b1.setPrefWidth(90);
        b1.setPrefHeight(30);
        b1.setOnAction(event -> run(event));

        Button clearButton = new Button("Clear");
        clearButton.setLayoutX(400);
        clearButton.setLayoutY(950);
        clearButton.setPrefWidth(90);
        clearButton.setPrefHeight(30);
        clearButton.setOnAction(event -> {

            pane.getChildren().removeIf(node -> node instanceof Circle || node instanceof Text && "dots".equals(node.getId()));
            pane.getChildren().removeIf(node -> node instanceof Line);
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            pane.getChildren().remove(t2);
            pane.getChildren().remove(t4);
            pane.getChildren().remove(t6);
            pane.getChildren().remove(t8);
            pane.getChildren().remove(t10);
            pane.getChildren().remove(t12);
            points.clear();
            lines.clear();
        });

        points = new ArrayList<>();
        pane.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();


            points p = new points(x,y);
            if (isPointWithinCanvas(p)) {
                points.add(new points(x,y));
                givedotscordinates(points, "p" + points.size(),points.size()-1);
            }
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
    public void run(ActionEvent event) {
        initialmemoryusage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        initialtime = System.currentTimeMillis();
        t2 = new Text(String.valueOf(initialtime) + " miliseconds");
        t2.setX(200);
        t2.setY(525);
        pane.getChildren().add(t2);

        t8 = new Text(String.valueOf(initialmemoryusage));
        t8.setX(200);
        t8.setY(750);
        pane.getChildren().add(t8);


        findConvexHull(points);

    }
    public boolean isPointWithinCanvas (points point){
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        return point.getX() >= 0 && point.getX() <= canvasWidth && point.getY() >= 0 && point.getY() <= canvasHeight;
    }
    public void givedotscordinates (ArrayList < points > p, String label,int i){
        createdots(p.get(i).getX(), p.get(i).getY(), label);
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

//    public void findConvexHull(ArrayList<points> points) {
//        ArrayList<points> convexHull = new ArrayList<>();
//        if (points.size() < 3){
//            return;
//        }
//
//
//        int minPoint = -1, maxPoint = -1;
//        double minX = Integer.MAX_VALUE;
//        double maxX = Integer.MIN_VALUE;
//        for (int i = 0; i < points.size(); i++) {
//            if (points.get(i).getX() < minX) {
//                minX = points.get(i).getX();
//                minPoint = i;
//            }
//            if (points.get(i).getX() > maxX) {
//                maxX = points.get(i).getX();
//                maxPoint = i;
//            }
//        }
//        points A = points.get(minPoint);
//        points B = points.get(maxPoint);
//        convexHull.add(A);
//        convexHull.add(B);
//        points.remove(A);
//        points.remove(B);
//
//        ArrayList<points> leftSet = new ArrayList<>();
//        ArrayList<points> rightSet = new ArrayList<>();
//
//        for (int i = 0; i < points.size(); i++) {
//            points p = points.get(i);
//            if (pointLocation(A, B, p) == -1)
//                leftSet.add(p);
//            else
//                rightSet.add(p);
//        }
//        hullSet(A, B, rightSet, convexHull);
//        hullSet(B, A, leftSet, convexHull);
//
//        for(int i=0 ; i<convexHull.size();i++){
//            points p1 = convexHull.get(i);
//            points p2 = convexHull.get((i+1)%convexHull.size());
//            drawLine(p1,p2);
//        }
//    }

    public void findConvexHull(ArrayList<points> points) {
        ArrayList<points> convexHull = new ArrayList<>();
        if (points.size() < 3){
            return;
        }


        int minPoint = -1, maxPoint = -1;
        double minX = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() < minX) {
                minX = points.get(i).getX();
                minPoint = i;
            }
            if (points.get(i).getX() > maxX) {
                maxX = points.get(i).getX();
                maxPoint = i;
            }
        }
        points A = points.get(minPoint);
        points B = points.get(maxPoint);
        Line line = drawLine1(A,B);
        lines.add(line);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);

        ArrayList<points> leftSet = new ArrayList<>();
        ArrayList<points> rightSet = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            points p = points.get(i);
            if (pointLocation(A, B, p) == -1)
                leftSet.add(p);
            else
                rightSet.add(p);
        }
        hullSet(A, B, rightSet, convexHull);
        hullSet(B, A, leftSet, convexHull);

        Timeline time = new Timeline();
        time.setCycleCount(1);
        time.setAutoReverse(false);
        for(int i = 0 ; i <lines.size();i++){
            Line l = lines.get(i);
            time.getKeyFrames().add(new KeyFrame(Duration.seconds(i),actionEvent -> pane.getChildren().add(l)));
        }

        time.setOnFinished(actionEvent -> {

            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(actionEvent1 -> {
                for(Line l : lines){
                    pane.getChildren().remove(l);
                }
            for(int i=0; i<convexHull.size();i++){
                points p1 = convexHull.get(i);
                points p2 = convexHull.get((i+1)%convexHull.size());
                drawLine(p1,p2);
            }
            });
            pause.play();

        });

        time.play();
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


//        for(int i=0 ; i<convexHull.size();i++){
//            points p1 = convexHull.get(i);
//            points p2 = convexHull.get((i+1)%convexHull.size());
//            drawLine(p1,p2);
//        }
    }


    public double distance(points A, points B, points C) {
        double ABx = B.getX() - A.getX();
        double ABy = B.getY() - A.getY();
        double num = ABx * (A.getY() - C.getY()) - ABy * (A.getX() - C.getX());
        if (num < 0)
            num = -num;
        return num;
    }

//    public void hullSet(points A, points B, ArrayList<points> set, ArrayList<points> hull) {
//        int insertPosition = hull.indexOf(B);
//        if (set.size() == 0)
//            return;
//        if (set.size() == 1) {
//            points p = set.get(0);
//            set.remove(p);
//            hull.add(insertPosition, p);
//            return;
//        }
//        double dist = Integer.MIN_VALUE;
//        int furthestPoint = -1;
//        for (int i = 0; i < set.size(); i++) {
//            points p = set.get(i);
//            double distance = distance(A, B, p);
//            if (distance > dist) {
//                dist = distance;
//                furthestPoint = i;
//            }
//        }
//        points P = set.get(furthestPoint);
//        set.remove(furthestPoint);
//        hull.add(insertPosition, P);
//
//        ArrayList<points> leftSetAP = new ArrayList<>();
//        for (int i = 0; i < set.size(); i++) {
//            points M = set.get(i);
//            if (pointLocation(A, P, M) == 1) {
//                leftSetAP.add(M);
//            }
//        }
//
//        ArrayList<points> leftSetPB = new ArrayList<>();
//        for (int i = 0; i < set.size(); i++) {
//            points M = set.get(i);
//            if (pointLocation(P, B, M) == 1) {
//                leftSetPB.add(M);
//            }
//        }
//        hullSet(A, P, leftSetAP, hull);
//        hullSet(P, B, leftSetPB, hull);
//
//    }
    public void hullSet(points A, points B, ArrayList<points> set, ArrayList<points> hull) {
        int insertPosition = hull.indexOf(B);
        if (set.size() == 0)
            return;
        if (set.size() == 1) {
            points p = set.get(0);
            set.remove(p);
            hull.add(insertPosition, p);
            Line l1 = drawLine1(A,B);
            lines.add(l1);
            Line l2 = drawLine1(A,p);
            lines.add(l2);
            Line l3 = drawLine1(B,p);
            lines.add(l3);
            return;
        }
        double dist = Integer.MIN_VALUE;
        int furthestPoint = -1;
        for (int i = 0; i < set.size(); i++) {
            points p = set.get(i);
            double distance = distance(A, B, p);
            if (distance > dist) {
                dist = distance;
                furthestPoint = i;
            }
        }
        points P = set.get(furthestPoint);
        set.remove(furthestPoint);
        hull.add(insertPosition, P);
        Line l1 = drawLine1(A,B);
        lines.add(l1);
        Line l2 = drawLine1(B,P);
        lines.add(l2);
        Line l4 = drawLine1(A,P);
        lines.add(l4);

        ArrayList<points> leftSetAP = new ArrayList<>();
        for (int i = 0; i < set.size(); i++) {
            points M = set.get(i);
            if (pointLocation(A, P, M) == 1) {
                leftSetAP.add(M);
            }
        }

        ArrayList<points> leftSetPB = new ArrayList<>();
        for (int i = 0; i < set.size(); i++) {
            points M = set.get(i);
            if (pointLocation(P, B, M) == 1) {
                leftSetPB.add(M);
            }
        }
        hullSet(A, P, leftSetAP, hull);
        hullSet(P, B, leftSetPB, hull);

    }
    public double pointLocation(points A, points B, points P) {
        double cp1 = (B.getX() - A.getX()) * (P.getY() - A.getY()) - (B.getY() - A.getY()) * (P.getX() - A.getX());
        if (cp1 > 0)
            return 1;
        else if (cp1 == 0)
            return 0;
        else
            return -1;
    }
    private void drawLine(points p1, points p2) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(event -> {
            System.out.println("Drawing line from (" + p1.getX() + "," + p1.getY() + ") to (" + p2.getX() + "," + p2.getY() + ")");
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(2);
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        });
        pause.play();
    }
    private Line drawLine1(points p1, points p2) {
        System.out.println("Drawing line from (" + p1.getX() + "," + p1.getY() + ") to (" + p2.getX() + "," + p2.getY() + ")");
        gc.setStroke(Color.RED);
        gc.setFill(Color.RED);
        gc.setLineWidth(2);

        Line l = new Line(p1.getX(),p1.getY(),p2.getX(),p2.getY());
        l.setStroke(Color.RED);
        l.setFill(Color.RED);
        l.setStrokeWidth(2);
      //  gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        return l;
    }
}
