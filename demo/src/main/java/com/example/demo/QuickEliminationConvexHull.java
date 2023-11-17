package com.example.demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class QuickEliminationConvexHull extends Application {
    ArrayList<points> points;
    LinkedList<points> convexhull;
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
        pane = new Pane();
        canvas = new Canvas(650, 500);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        Scene scene = new Scene(pane, 650, 1000);

        stage.setTitle("Quick Elimination Convex Hull");
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

    public void findConvexHull(ArrayList<points> points){
        int n = points.size();

        if(n<3){
            return;
        }

        points leftmost,rightmost,upmost,downmost;
        int left=0;
        int right =0;
        int up=0;
        int down =0;
        for(int i =0 ; i< n ; i++){
            if(points.get(i).getX()<points.get(left).getX() || (points.get(i).getX() == points.get(left).getX() && points.get(i).getY()>points.get(left).getY())){
                left =i;
            }
        }
        leftmost = points.get(left);
        for(int i =0 ; i< n ; i++){
            if(points.get(i).getX()>points.get(right).getX() || (points.get(i).getX() == points.get(right).getX() && points.get(i).getY()<points.get(right).getY())){
                right =i;
            }
        }
        rightmost = points.get(right);
        for(int i =0 ; i< n ; i++){
            if(points.get(i).getY()<points.get(down).getY() || (points.get(i).getY() == points.get(down).getY() && points.get(i).getX()>points.get(down).getX())){
                down =i;
            }
        }
        downmost = points.get(down);
        for(int i =0 ; i< n ; i++){
            if(points.get(i).getY()>points.get(up).getY() || (points.get(i).getY() == points.get(up).getY() && points.get(i).getX()>points.get(up).getX())){
                up =i;
            }
        }
        upmost = points.get(up);

        drawLine(leftmost,upmost);
        drawLine(upmost,rightmost);
        drawLine(rightmost,downmost);
        drawLine(downmost,leftmost);

        ArrayList <points> inside = new ArrayList<>();
        ArrayList <points> outside = new ArrayList<>();
        ArrayList<Circle> insidedots = new ArrayList<>();
        ArrayList<points> polygon = new ArrayList<>();
        polygon.add(upmost);
        polygon.add(downmost);
        polygon.add(rightmost);
        polygon.add(leftmost);
        for(points point : points){
            if(point != upmost && point!= rightmost && point != leftmost && point != downmost) {
                if (isPointInsidePolygon(point, polygon)) {
                    inside.add(point);
                } else {
                    outside.add(point);
                    System.out.println("Points outside the polygons "+point.getX()+" "+point.getY());
                }
            }
        }

        for(points inner : inside){
            Circle dots = new Circle(inner.getX(),inner.getY(),5);
            dots.setStroke(Color.BLUE);
            pane.getChildren().add(dots);
            insidedots.add(dots);

        }


    }
    public boolean isPointInsidePolygon(points point, ArrayList<points> polygon) {
    int n = polygon.size();
    int intersectCount = 0;
    double epsilon = 1e-20;

    for (int i = 0; i < n; i++) {
        points p1 = polygon.get(i);
        points p2 = polygon.get((i + 1) % n);

        // Check if the ray crosses the edge
        if ((p1.getY() < point.getY()) != (p2.getY() < point.getY()) &&
                (point.getX() < (p2.getX() - p1.getX()) * (point.getY() - p1.getY()) / (p2.getY() - p1.getY()+epsilon) + p1.getX())) {
            intersectCount++;
        }
    }
    return intersectCount % 2 == 1;
}



public boolean doIntersect(points p1, points q1, points p2, points q2) {
    int o1 = orientation(p1, q1, p2);
    int o2 = orientation(p1, q1, q2);
    int o3 = orientation(p2, q2, p1);
    int o4 = orientation(p2, q2, q1);

    if (o1 != o2 && o3 != o4) {
        return true;
    }

    if (o1 == 0 && onSegment(p1, p2, q1)) return true;
    if (o2 == 0 && onSegment(p1, q2, q1)) return true;
    if (o3 == 0 && onSegment(p2, p1, q2)) return true;
    if (o4 == 0 && onSegment(p2, q1, q2)) return true;

    return false;
}

public boolean onSegment(points p, points q, points r) {
    if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
        q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY())) {
        return true;
    }
    return false;
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
    private static int orientation(points p, points q, points r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) return 0;  // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or counterclockwise
    }

    private void drawLine(points p1, points p2) {
        System.out.println("Drawing line from (" + p1.getX() + "," + p1.getY() + ") to (" + p2.getX() + "," + p2.getY() + ")");
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    public static void main(String[] args) {
        launch();
    }
}
