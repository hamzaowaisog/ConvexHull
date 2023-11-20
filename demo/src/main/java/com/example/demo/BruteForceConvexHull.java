package com.example.demo;

import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
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
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class BruteForceConvexHull extends Application {
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
        pane = new Pane();
        lines = new ArrayList<>();
        canvas = new Canvas(650, 500);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        Scene scene = new Scene(pane, 650, 1000);

        stage.setTitle("Brute Force Convex Hull");
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
            lines.clear();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            pane.getChildren().remove(t2);
            pane.getChildren().remove(t4);
            pane.getChildren().remove(t6);
            pane.getChildren().remove(t8);
            pane.getChildren().remove(t10);
            pane.getChildren().remove(t12);
            points.clear();
            ij.clear();
            jk.clear();
            i=0;
            j=0;
            k=0;
        });
        Button back = new Button("Back");
        back.setLayoutX(200);
        back.setLayoutY(950);
        back.setPrefWidth(90);
        back.setPrefHeight(30);
        back.setOnAction(actionEvent ->{
            Stage s1 = new Stage();
            ConvexHull hull = new ConvexHull();
            try {
                points.clear();
                ij.clear();
                jk.clear();
                hull.start(s1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            stage.close();

        });

        points = new ArrayList<>();
        pane.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();


            points p = new points(x, y);
            if (isPointWithinCanvas(p)) {
                points.add(new points(x, y));
                givedotscordinates(points, "p" + points.size(), points.size() - 1);
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
        pane.getChildren().add(back);

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
        findConvexHull1(points);
        //findConvexHull(points);
//        points p0 = getLowestPoint(convexHull);
//        convexHull.sort((p1,p2) -> comparePoints(p0,p1,p2));
//        for (int i = 0; i < convexHull.size(); i++) {
//            points p1 = convexHull.get(i);
//            points p2 = convexHull.get((i + 1) % convexHull.size());
//            drawLine(p1, p2);
//        }


    }
    boolean shouldContinue = true;
    Stack<Line> ij = new Stack<>();
    Stack<Line> jk = new Stack<>();
    Timeline tl = new Timeline();
    int i = 0, j = 0, k = 0;
    ArrayList<points> convexhull ;

    private void findConvexHull1(ArrayList<com.example.demo.points> points) {
        convexhull = points;
        tl.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(Duration.seconds(0.01), actionEvent -> {
            if(i<points.size() && j<points.size() && k < points.size()) {
                if (points.get(i) != points.get(j)) {
                    drawLineIJ(points.get(i), points.get(j));
                    if (points.get(k) != points.get(j) && points.get(k) != points.get(i)) {
                        drawLineJK(points.get(j), points.get(k));
                        if (ccwSlope(points.get(i), points.get(j), points.get(k)) == 1) {
                           pane.getChildren().remove(ij.pop());
                            shouldContinue = false;
                        }
                        pane.getChildren().remove(jk.pop());
                    }
                    k++;
                    if (!shouldContinue) {
                        j++;
                        k = 0;
                        shouldContinue = true;
                    }
                } else {
                    j++;
                }
            }
                if (k >= points.size() && shouldContinue) {
                   // drawLineIJ(points.get(i), points.get(j));
                }
                if (k >= points.size()) {
                    j++;
                    k = 0;
                    shouldContinue = true;
                }
                if (j >= points.size()) {
                    i++;
                    j = 0;
                }
                if (i >= points.size()) {
                    tl.stop();
                    gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                    for(Line l : ij){
                        pane.getChildren().remove(l);
                    }
                    for(int ii = 0 ; ii < convexhull.size() ; ii++){
                        for (int jj = 0 ; jj < convexhull.size() ; jj++){
                            if(convexhull.get(ii)!=convexhull.get(jj)){
                                //draw temp line ij
                                for(int kk = 0 ; kk < convexhull.size() ; kk++){
                                    shouldContinue = true;
                                    if(convexhull.get(kk) != convexhull.get(jj) && convexhull.get(kk)!=convexhull.get(ii)){
                                        //draw line jk
                                        if(ccwSlope(convexhull.get(ii),convexhull.get(jj),convexhull.get(kk))==-1){
                                            //remove line jk
                                            shouldContinue = false;
                                        }
                                        //remove line ij
                                        if(!shouldContinue){
                                            break;
                                        }
                                    }
                                }
                            }
                            if(shouldContinue){
                                //perma line ij
                                points p11 = new points(convexhull.get(ii).getX(),convexhull.get(ii).getY());
                                com.example.demo.points p2 = new points(convexhull.get(jj).getX(),convexhull.get(jj).getY());
                                drawLine(p11,p2);
                            }
                        }
                    }
                }
        });
        tl.getKeyFrames().add(kf);
        tl.play();

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

    private void drawLineIJ(points a, points b) {
        if(ij.isEmpty()){
            Line l1 = new Line(a.getX(), a.getY(), b.getX(), b.getY());
            l1.setStroke(Color.RED);
            l1.setStrokeWidth(2);
            pane.getChildren().add(l1);
            ij.push(l1);
        }
        if((a.getX()!=ij.peek().getStartX() && a.getY()!=ij.peek().getStartY()) || (b.getX()!=ij.peek().getEndX() && b.getY()!=ij.peek().getEndY()) ){
            Line l1 = new Line(a.getX(), a.getY(), b.getX(), b.getY());
            l1.setStroke(Color.RED);
            l1.setStrokeWidth(2);
            pane.getChildren().add(l1);
            ij.push(l1);
        }
    }
    private void drawLineJK(points a, points b) {
        Line l1 = new Line(a.getX(), a.getY(), b.getX(), b.getY());
        l1.setStroke(Color.RED);
        l1.setStrokeWidth(2);
        pane.getChildren().add(l1);
        jk.push(l1);
    }


    public boolean isPointWithinCanvas(points point) {
    double canvasWidth = canvas.getWidth();
    double canvasHeight = canvas.getHeight();
    return point.getX() >= 0 && point.getX() <= canvasWidth && point.getY() >= 0 && point.getY() <= canvasHeight;
}
    public void givedotscordinates(ArrayList<points> p,String label,int i){
        createdots(p.get(i).getX(),p.get(i).getY(),label);
    }
    public void createdots(double x, double y,String label){
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

//    public void findConvexHull(ArrayList<points> pointsList) {
//    int n = pointsList.size();
//    ArrayList<points> convexHull = new ArrayList<>();
//
//    if (n < 3) {
//        return;
//    }
//
//    int l = 0;
//    for (int i = 1; i < n; i++) {
//        if (pointsList.get(i).getX() < pointsList.get(l).getX()) {
//            l = i;
//        }
//    }
//    int p = l, q;
//
//    Timeline timeline = new Timeline();
//    timeline.setCycleCount(Timeline.INDEFINITE);
//    lines = new ArrayList<>();
//    int finalL = l;
//    KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
//        int p = finalL, q;
//        int i = 0;
//        @Override
//        public void handle(ActionEvent event) {
//            if (i < n) {
//                if (pointsList.get(i) != pointsList.get(p)) {
//                    // Draw a line from the current point to all other points
//                    Line line = new Line(pointsList.get(p).getX(), pointsList.get(p).getY(), pointsList.get(i).getX(), pointsList.get(i).getY());
//                    line.setStroke(Color.RED);
//                    pane.getChildren().add(line);
//                    lines.add(line);
//                }
//
//                if (orientation(pointsList.get(p), pointsList.get(i), pointsList.get(q)) == 2)
//                    q = i;
//
//                i++;
//            } else {
//                // Remove all lines except the one between the current point and the next point
//                for (Line line : lines) {
//                    if (line.getEndX() != pointsList.get(q).getX() || line.getEndY() != pointsList.get(q).getY()) {
//                        pane.getChildren().remove(line);
//                    }
//                }
//
//                convexHull.add(pointsList.get(p));
//                p = q;
//                q = (p + 1) % n;
//                i = 0;
//                lines.clear();
//
//                if (p == finalL) {
//                    timeline.stop();
//                    // Clear the canvas
//                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//
//                    // Draw the final convex hull
//                    for (int j = 0; j < convexHull.size(); j++) {
//                        points p1 = convexHull.get(j);
//                        points p2 = convexHull.get((j + 1) % convexHull.size());
//                        drawLine(p1, p2);
//                    }
//                }
//            }
//        }
//    });
//
//    timeline.getKeyFrames().add(keyFrame);
//    timeline.play();
//
//        finaltime = System.currentTimeMillis();
//        timecomplexity = finaltime - initialtime;
//        t4 = new Text(String.valueOf(finaltime)+" miliseconds");
//        t4.setX(200);
//        t4.setY(600);
//        pane.getChildren().add(t4);
//
//        t6 = new Text(String.valueOf(timecomplexity)+" miliseconds");
//        t6.setX(200);
//        t6.setY(675);
//        pane.getChildren().add(t6);
//
//        finalmemoryusage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        memorycomplexity = finalmemoryusage - initialmemoryusage;
//        t10 = new Text(String.valueOf(finalmemoryusage));
//        t10.setX(200);
//        t10.setY(825);
//        pane.getChildren().add(t10);
//
//        t12 = new Text(String.valueOf(memorycomplexity));
//        t12.setX(200);
//        t12.setY(900);
//        pane.getChildren().add(t12);
//}

//    public void findConvexHull(ArrayList<points> points){
//        ArrayList<points> convexhull = new ArrayList<>();
//        for(int i = 0 ; i < points.size() ; i++){
//            for (int j = 0 ; j < points.size() ; j++){
//                if(points.get(i)!=points.get(j)){
//                    //draw temp line ij
//                    for(int k = 0 ; k < points.size() ; k++){
//                        shouldContinue = true;
//                        if(points.get(k) != points.get(j) && points.get(k)!=points.get(i)){
//                            //draw line jk
//                            if(ccwSlope(points.get(i),points.get(j),points.get(k))==-1){
//                                //remove line jk
//                                shouldContinue = false;
//                            }
//                            //remove line ij
//                            if(!shouldContinue){
//                                break;
//                            }
//                        }
//                    }
//                }
//                if(shouldContinue){
//                    //perma line ij
//                    points p1 = new points(points.get(i).getX(),points.get(i).getY());
//                    com.example.demo.points p2 = new points(points.get(j).getX(),points.get(j).getY());
//                   drawLine(p1,p2);
//                }
//            }
//        }
//   }


//    public void findConvexHull(ArrayList<points> points){
//    ArrayList<points> convexhull = new ArrayList<>();
//
//    for(int i = 0 ; i < points.size() ; i++){
//        for (int j = 0 ; j < points.size() ; j++){
//            if(points.get(i)!=points.get(j)){
//                for(int k = 0 ; k < points.size() ; k++){
//                    shouldContinue = true;
//                    if(points.get(k) != points.get(j) && points.get(k)!=points.get(i)){
//                        if(ccwSlope(points.get(i),points.get(j),points.get(k))==-1){
//                            shouldContinue = false;
//                        }
//                        if(!shouldContinue){
//                            break;
//                        }
//                    }
//                }
//            }
//            if(shouldContinue){
//                points p1 = new points(points.get(i).getX(),points.get(i).getY());
//                com.example.demo.points p2 = new points(points.get(j).getX(),points.get(j).getY());
//                convexhull.add(p1);
//                convexhull.add(p2);
//            }
//        }
//    }
//
//    // Create a new timeline
//    Timeline timeline = new Timeline();
//
//    // For each pair of points in the convex hull
//    for (int i = 0; i < convexhull.size() - 1; i += 2) {
//        points p1 = convexhull.get(i);
//        points p2 = convexhull.get(i + 1);
//
//        // Create a keyframe that draws a line between the two points
//        KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 0.5), e -> drawLine(p1, p2));
//
//        // Add the keyframe to the timeline
//        timeline.getKeyFrames().add(keyFrame);
//    }
//
//    // Play the timeline
//    timeline.play();
//}

    public double ccwSlope(points p0, points p1, points p2){
        double dx1,dx2,dy1,dy2;
        dx1 = p1.getX() - p0.getX();
        dy1 = p1.getY() - p0.getY();
        dx2 = p2.getX() - p0.getX();
        dy2 = p2.getY() - p0.getY();
        if(dx1*dy2 > dy1*dx2){
            return 1;
        }
        if (dx1*dy2 < dy1*dx2){
            return -1;
        }
        if ( (dx1*dx2 < 0) || (dy1*dy2 < 0) ){
            return -1;
        }
        if( (dx1*dx1 + dy1*dy1) < (dx2*dx2 + dy2*dy2) ){
            return 1;
        }
        return 0;
    }
private static int orientation(points p, points q, points r) {
    double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
    if (val == 0) return 0;  // Collinear
    return (val > 0) ? 1 : 2; // Clockwise or counterclockwise
}


    private void drawLine(points p1, points p2) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    public Line drawline1(points p1,points p2){
        Line l1 = new Line(p1.getX(),p1.getY(),p2.getX(),p2.getY());
        l1.setStroke(Color.RED);
        pane.getChildren().add(l1);
        return l1;

    }
}
