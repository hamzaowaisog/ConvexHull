package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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

import java.lang.reflect.Array;
import java.util.*;

public class GrahamScanConvexHull extends Application {
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

        private void findConvexHull(ArrayList<points> points){
        ArrayList<points> convexhull = new ArrayList<>();
        ArrayList<points> sortedpoints = new ArrayList<>();
        Stack <points> convex  = new Stack<>();
        int n = points.size();
        if(n<3){
            return;
        }

        points p0 = points.get(0);
        for(points p : points){
            if(p.getY() < p0.getY() || (p.getY() == p0.getY() && p.getX()<p0.getX())){
                p0 = p;
            }
        }
        int down =0;
        for(int i=0 ; i<n ;i++){
            if(points.get(i).getY() < points.get(down).getY() || (points.get(i).getY() == points.get(down).getY() && points.get(i).getX()< points.get(down).getX())){
                down = i;
            }
        }


        sortedpoints = comparator(points,p0);
        convex.push(sortedpoints.get(0));
        convex.push(sortedpoints.get(1));
        convex.push(sortedpoints.get(2));

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        lines= new ArrayList<>();
        int finaldown = down;
        ArrayList<points> finalsortedpointd = sortedpoints;
        Stack <points> finalconvex = convex;
        ArrayList<points> finalconvexhull = convexhull;
            final int[] finali = {3};
            ArrayList<com.example.demo.points> finalSortedpoints = sortedpoints;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(finali[0] < n && finali[0] < finalsortedpointd.size()){
                   points p0 = finalconvex.pop();
                   Line line =new Line(nexttotop(finalconvex).getX(),nexttotop(finalconvex).getY(),finalconvex.peek().getX(),finalconvex.peek().getY());
                   line.setStroke(Color.RED);
                   pane.getChildren().add(line);
                   lines.add(line);
                   finalconvex.push(p0);
                   line = new Line(nexttotop(finalconvex).getX(),nexttotop(finalconvex).getY(),finalconvex.peek().getX(),finalconvex.peek().getY());
                   line.setStroke(Color.RED);
                   pane.getChildren().add(line);
                   lines.add(line);
                    if (finali[0] >= 3) {
                        line = new Line(finalconvex.peek().getX(), finalconvex.peek().getY(), finalSortedpoints.get(finali[0]).getX(), finalSortedpoints.get(finali[0]).getY());
                        line.setStroke(Color.GREEN);
                        pane.getChildren().add(line);
                        lines.add(line);
                    }


                   while(orientation(nexttotop(convex),convex.peek(), finalSortedpoints.get(finali[0])) == 1){
                       points pop = convex.pop();
                       for(Line line1 : lines){
                           if(line1.getEndX() ==  pop.getX() || line1.getEndY() == pop.getY() ){
                               pane.getChildren().remove(line1);
                           }
                       }
                       if (finali[0] >= 3) {
                           Line lineToRemove = lines.get(lines.size() - 1);
                           pane.getChildren().remove(lineToRemove);
                           lines.remove(lineToRemove);
                       }
                       if(finali[0] == n || finali[0] == finalSortedpoints.size()){
                           line = new Line(finalconvex.peek().getX(), finalconvex.peek().getY(), finalSortedpoints.get(0).getX(), finalSortedpoints.get(0).getY());
                           line.setStroke(Color.GREEN);
                           pane.getChildren().add(line);
                           lines.add(line);
                       }

                   }
                   finalconvex.push(finalsortedpointd.get(finali[0]));
                    finali[0]++;
                }


                if(finali[0] == n){
                    timeline.stop();
                    for(Line line : lines) {
                        pane.getChildren().remove(line);
                    }
                    gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

                    while(!convex.isEmpty()) {
                        convexhull.add(convex.pop());
                    }

                        for (int i =0 ; i<convexhull.size();i++){
                            points p1 = convexhull.get(i);
                            points p2 = convexhull.get((i+1) % convexhull.size());
                            drawLine(p1,p2);
                        }
                    }
                }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

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
//    private void findConvexHull(ArrayList<points> points){
//        ArrayList<points> convexhull = new ArrayList<>();
//        ArrayList<points> sortedpoints = new ArrayList<>();
//        Stack <points> convex  = new Stack<>();
//        int n = points.size();
//        if(n<3){
//            return;
//        }
//
//        points p0 = points.get(0);
//        for(points p : points){
//            if(p.getY() < p0.getY() || (p.getY() == p0.getY() && p.getX()<p0.getX())){
//                p0 = p;
//            }
//        }
//
//        sortedpoints = comparator(points,p0);
//        convex.push(sortedpoints.get(0));
//        convex.push(sortedpoints.get(1));
//        convex.push(sortedpoints.get(2));
//
//        for(int i = 3 ; i< n;i++){
//            while(orientation(nexttotop(convex),convex.peek(),sortedpoints.get(i)) == 1){
//                convex.pop();
//            }
//            convex.push(sortedpoints.get(i));
//        }
//
//        while(!convex.isEmpty()){
//            convexhull.add(convex.pop());
//        }
//
//        for (int i =0 ; i<convexhull.size();i++){
//            points p1 = convexhull.get(i);
//            points p2 = convexhull.get((i+1) % convexhull.size());
//            drawLine(p1,p2);
//        }
//
//    }
    private points nexttotop(Stack<points> stack){

        if(stack.size()>=2) {
            points p = stack.pop();
            points res = stack.peek();
            stack.push(p);
            return res;
        }
        return null;
    }

    public ArrayList<points> comparator (ArrayList<points> points , points p0){

       Comparator<points> polarangle = new Comparator<points>() {
           @Override
           public int compare(com.example.demo.points p1, com.example.demo.points p2) {
               double angle1 = Math.atan2(p1.getY()- p0.getY(),p1.getX()- p0.getX());
               double angle2 = Math.atan2(p2.getY()-p0.getY(),p2.getX()- p0.getX());

               if(angle1<angle2){
                   return -1;
               }
               else if(angle1>angle2){
                   return 1;
               }
               else{
                   return 0;
               }
           }
       };
       Collections.sort(points,polarangle);
       return points;
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
