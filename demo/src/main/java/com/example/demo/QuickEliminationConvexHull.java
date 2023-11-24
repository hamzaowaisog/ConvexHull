package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
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
import java.util.Comparator;
import java.util.LinkedList;

public class QuickEliminationConvexHull extends Application {
    ArrayList<points> points;
    ArrayList<points> convexhull;
    ArrayList<Line> lines;
    ArrayList<Line> lines2;
    ArrayList<Line> lines3;
    ArrayList<Line> lines4;
    ArrayList<Line> lines5;
    ArrayList<Circle> dots1;
    ArrayList<Circle> dots2;
    ArrayList<Circle> dots3;
    ArrayList<Circle> dots4;
    ArrayList<Circle> dots5;
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
        lines2 = new ArrayList<>();
        lines3 = new ArrayList<>();
        lines4 = new ArrayList<>();
        lines5 = new ArrayList<>();
        dots1 = new ArrayList<>();
        dots2 = new ArrayList<>();
        dots3 = new ArrayList<>();
        dots4 = new ArrayList<>();
        dots5 = new ArrayList<>();
        convexhull = new ArrayList<>();
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
            lines.clear();
            lines2.clear();
            lines3.clear();
            lines4.clear();
            lines5.clear();
            dots1.clear();
            dots2.clear();
            dots3.clear();
            dots4.clear();
            dots5.clear();
            region1.clear();
            region2.clear();
            region3.clear();
            region4.clear();
            convexhull.clear();
            hull.clear();
        });

        Button back = new Button("Back");
        back.setLayoutX(200);
        back.setLayoutY(950);
        back.setPrefWidth(90);
        back.setPrefHeight(30);
        back.setOnAction(actionEvent ->{
            hull.clear();
            points.clear();
            lines.clear();
            lines2.clear();
            lines3.clear();
            lines4.clear();
            lines5.clear();
            dots1.clear();
            dots2.clear();
            dots3.clear();
            dots4.clear();
            dots5.clear();
            region1.clear();
            region2.clear();
            region3.clear();
            region4.clear();
            convexhull.clear();
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

//    public void findConvexHull(ArrayList<points> points){
//        int n = points.size();
//
//        if(n<3){
//            return;
//        }
//
//        points leftmost,rightmost,upmost,downmost;
//        int left=0;
//        int right =0;
//        int up=0;
//        int down =0;
//        for(int i =0 ; i< n ; i++){
//            if(points.get(i).getX()<points.get(left).getX() || (points.get(i).getX() == points.get(left).getX() && points.get(i).getY()>points.get(left).getY())){
//                left =i;
//            }
//        }
//        leftmost = points.get(left);
//        for(int i =0 ; i< n ; i++){
//            if(points.get(i).getX()>points.get(right).getX() || (points.get(i).getX() == points.get(right).getX() && points.get(i).getY()<points.get(right).getY())){
//                right =i;
//            }
//        }
//        rightmost = points.get(right);
//        for(int i =0 ; i< n ; i++){
//            if(points.get(i).getY()<points.get(down).getY() || (points.get(i).getY() == points.get(down).getY() && points.get(i).getX()>points.get(down).getX())){
//                down =i;
//            }
//        }
//        downmost = points.get(down);
//        for(int i =0 ; i< n ; i++){
//            if(points.get(i).getY()>points.get(up).getY() || (points.get(i).getY() == points.get(up).getY() && points.get(i).getX()>points.get(up).getX())){
//                up =i;
//            }
//        }
//        upmost = points.get(up);
//
//        drawLine(leftmost,upmost);
//        drawLine(upmost,rightmost);
//        drawLine(rightmost,downmost);
//        drawLine(downmost,leftmost);
//
//        ArrayList <points> inside = new ArrayList<>();
//        ArrayList <points> outside = new ArrayList<>();
//        ArrayList<Circle> insidedots = new ArrayList<>();
//        ArrayList<points> polygon = new ArrayList<>();
//        polygon.add(upmost);
//        polygon.add(downmost);
//        polygon.add(rightmost);
//        polygon.add(leftmost);
//        for(points point : points){
//            if(point != upmost && point!= rightmost && point != leftmost && point != downmost) {
//                if (isPointInsidePolygon(point, polygon)) {
//                    inside.add(point);
//                    System.out.println("Points inside the polygons "+point.getX()+" "+point.getY());
//                } else {
//                    outside.add(point);
//                    System.out.println("Points outside the polygons "+point.getX()+" "+point.getY());
//                }
//            }
//        }
//
//        for(points inner : inside){
//            Circle dots = new Circle(inner.getX(),inner.getY(),5);
//            dots.setStroke(Color.BLUE);
//            pane.getChildren().add(dots);
//            insidedots.add(dots);
//
//        }
//
//
//    }
//public boolean isPointInsidePolygon(points point, ArrayList<points> polygon) {
//    int n = polygon.size();
//    int intersectCount = 0;
//    double epsilon = 1e-20;
//
//    for (int i = 0; i < n; i++) {
//        points p1 = polygon.get(i);
//        points p2 = polygon.get((i + 1) % n);
//
//        // Check if the point is on the edge
//        if (isPointOnLineSegment(p1, point, p2)) {
//            return true;
//        }
//
//        // Check if the ray crosses the edge
//        if ((p1.getY() < point.getY()) != (p2.getY() < point.getY())) {
//            double intersectX = p1.getX() + (point.getY() - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY());
//
//            // Check if the ray intersects with a vertex
//            if (Math.abs(intersectX - point.getX()) < epsilon) {
//                if ((p1.getY() < point.getY() && p2.getY() > point.getY()) || (p1.getY() > point.getY() && p2.getY() < point.getY())) {
//                    intersectCount += 2;
//                } else {
//                    intersectCount++;
//                }
//            } else if (intersectX > point.getX()) {
//                intersectCount++;
//            }
//        }
//    }
//    return intersectCount % 2 == 1;
//}
//
//public boolean doIntersect(points p1, points q1, points p2, points q2) {
//    int o1 = orientation(p1, q1, p2);
//    int o2 = orientation(p1, q1, q2);
//    int o3 = orientation(p2, q2, p1);
//    int o4 = orientation(p2, q2, q1);
//
//    if (o1 != o2 && o3 != o4) {
//        return true;
//    }
//
//    if (o1 == 0 && isPointOnLineSegment(p1, p2, q1)) return true;
//    if (o2 == 0 && isPointOnLineSegment(p1, q2, q1)) return true;
//    if (o3 == 0 && isPointOnLineSegment(p2, p1, q2)) return true;
//    if (o4 == 0 && isPointOnLineSegment(p2, q1, q2)) return true;
//
//    return false;
//}


     points temp;
     points i;
     points j;
     double maxY;
     int maxYIdx = -1;
     points maxYPoint;
    private points findMaxY(ArrayList<points> points) {
        maxY = points.get(0).getY();
        maxYIdx = 0;
        maxYPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).getY() > maxY) {
                maxY = points.get(i).getY();
                maxYIdx = i;
                maxYPoint = points.get(i);
            }
        }
        return maxYPoint;
    }
     double minY;
     int minYIdx = -1;
     points minYPoint;
    private points findMinY(ArrayList<points> points) {
        minY = points.get(0).getY();
        minYIdx = 0;
        minYPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).getY() < minY) {
                minY = points.get(i).getY();
                minYIdx = i;
                minYPoint = points.get(i);
            }
        }
        return minYPoint;
    }
     double maxX;
     int maxXIdx = -1;
    points maxXPoint;
    private points findMaxX(ArrayList<points> points) {
        maxX = points.get(0).getX();
        maxXIdx = 0;
        maxXPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).getX() > maxX) {
                maxX = points.get(i).getX();
                maxXIdx = i;
                maxXPoint = points.get(i);
            }
        }
        return maxXPoint;
    }
    double minX;
    int minXIdx = -1;
    points minXPoint;
    private points findMinX(ArrayList<points> points) {
        minX = points.get(0).getX();
        minXIdx = 0;
        minXPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).getX() < minX) {
                minX = points.get(i).getX();
                minXIdx = i;
                minXPoint = points.get(i);
            }
        }
        return minXPoint;
    }
     ArrayList<points> hull = new ArrayList<>();
     ArrayList<points> region1 = new ArrayList<>();
     ArrayList<points> region2 = new ArrayList<>();
     ArrayList<points> region3 = new ArrayList<>();
     ArrayList<points> region4 = new ArrayList<>();
     int jItr;
     int tempItr;

    public void findConvexHull(ArrayList<points> points){
        hull.add(findMinX(points));
        hull.add(findMinY(points));
        hull.add(findMaxX(points));
        hull.add(findMaxY(points));

       lines.add(drawLine1(hull.get(0), hull.get(1)));
       lines.add(drawLine1(hull.get(1), hull.get(2)));
       lines.add(drawLine1(hull.get(2), hull.get(3)));
       lines.add(drawLine1(hull.get(3), hull.get(0)));

        // all points are inside quad
        for(int i = 0 ; i < points.size() ; i++){
            if(hull.contains(points.get(i))) {
                points.get(i).isOnHull = true;
            }
            points.get(i).flag = false;
            Circle c1 = drawdots1(points.get(i));
            dots1.add(c1);
        }

        // check if any point in region 1, remove it from quad

        for(int i = 0 ; i < points.size() ; i++){
            if(points.get(i)!=hull.get(0) && points.get(i)!=hull.get(1)){
                if(area(hull.get(0), hull.get(1), points.get(i))<0){
                    points.get(i).flag = true;
                    points pointToRemove = points.get(i);
                    for (int j = 0; j < dots1.size(); j++) {
                        if ((dots1.get(j).getCenterX() == pointToRemove.getX()) && (dots1.get(j).getCenterY() == pointToRemove.getY())) {
                            dots1.remove(j);
                            break;
                        }
                    }
                }
            }
        }
        // check if any point in region 2, remove it from quad

        for(int i = 0 ; i < points.size() ; i++){
            if(points.get(i)!=hull.get(1) && points.get(i)!=hull.get(2)){
                if(area(hull.get(1), hull.get(2), points.get(i))<0){
                    points.get(i).flag = true;
                    points pointToRemove = points.get(i);
                    for (int j = 0; j < dots1.size(); j++) {
                        if ((dots1.get(j).getCenterX() == pointToRemove.getX()) && (dots1.get(j).getCenterY() == pointToRemove.getY())) {
                            dots1.remove(j);
                            break;
                        }
                    }
                }
            }
        }
        // check if any point in region 3, remove it from quad

        for(int i = 0 ; i < points.size() ; i++) {
            if (points.get(i) != hull.get(2) && points.get(i) != hull.get(3)) {
                if (area(hull.get(2), hull.get(3), points.get(i)) < 0) {
                    points.get(i).flag = true;
                    points pointToRemove = points.get(i);
                    for (int j = 0; j < dots1.size(); j++) {
                        if ((dots1.get(j).getCenterX() == pointToRemove.getX()) && (dots1.get(j).getCenterY() == pointToRemove.getY())) {
                            dots1.remove(j);
                            break;
                            }
                        }
                }
            }
        }
        // check if any point in region 4, remove it from quad

        for(int i = 0 ; i < points.size() ; i++){
            if(points.get(i)!=hull.get(3) && points.get(i)!=hull.get(0)){
                if(area(hull.get(3), hull.get(0), points.get(i))<0){
                    points.get(i).flag = true;
                    points pointToRemove = points.get(i);
                    for (int j = 0; j < dots1.size(); j++) {
                        if ((dots1.get(j).getCenterX() == pointToRemove.getX()) && (dots1.get(j).getCenterY() == pointToRemove.getY())) {
                            dots1.remove(j);
                            break;
                        }
                    }
                }
            }
        }

        //points getting removed from region ALL RIGHT

        // add to region 1 array
        region1.add(hull.get(0));
        region1.add(hull.get(1));
        for (int i = 0; i < points.size(); i++) {
            if( points.get(i)!=region1.get(0) && points.get(i)!=region1.get(1) && points.get(i).flag && area(region1.get(0),region1.get(1), points.get(i))<0 ){
                region1.add(points.get(i));
            }
        }
        region1.sort(Comparator.comparingDouble(com.example.demo.points::getX));



        // add to region 2 array
        region2.add(hull.get(1));
        region2.add(hull.get(2));
        for (int i = 0; i < points.size(); i++) {
            if( points.get(i)!=region1.get(0) && points.get(i)!=region2.get(1) && points.get(i).flag && area(region2.get(0), region2.get(1), points.get(i))<0 ){
                region2.add(points.get(i));
            }
        }
        region2.sort(Comparator.comparingDouble(com.example.demo.points::getX));


        // add to region 3 array
        region3.add(hull.get(2));
        region3.add(hull.get(3));
        for (int i = 0; i < points.size(); i++) {
            if( points.get(i)!=region3.get(0) && points.get(i)!=region3.get(1) && points.get(i).flag && area(region3.get(0), region3.get(1), points.get(i))<0 ){
                region3.add(points.get(i));
            }
        }
        region3.sort(Comparator.comparingDouble(com.example.demo.points::getX).reversed());


        // add to region 4 array
        region4.add(hull.get(3));
        region4.add(hull.get(0));
        for (int i = 0; i < points.size(); i++) {
            if( points.get(i)!=region4.get(0) && points.get(i)!=region4.get(1) && points.get(i).flag && area(region4.get(0), region4.get(1), points.get(i))<0 ){
                region4.add(points.get(i));
            }
        }
        region4.sort(Comparator.comparingDouble(com.example.demo.points::getX).reversed());

        // REGIONS ALL RIGHT

        //get hull of region1
        if(region1.size()>3){
            jItr = 2;
            tempItr=0;
            temp = region1.get(tempItr);
            i = region1.get(1);
            j = region1.get(jItr);
            while(j != region1.get(region1.size()-1)){
                if(area(temp, i ,j)>=0){
                    tempItr++;
                    temp = region1.get(tempItr);
                    i = j;
                    jItr++;
                    j = region1.get(jItr);
                }else{
                    dots2.add(drawdots1(i));
                    region1.remove(i);
                    if(tempItr-1<0){
                        tempItr=0;
                        i=j;
                        j=region1.get(jItr);
                    }else{
                        jItr--;
                        i = region1.get(tempItr);
                        tempItr--;
                        temp = region1.get(tempItr);
                    }
                }
            }
            if(area(temp, i ,j )<0){
                points p1 = i;
                Circle c1 = drawdots1(p1);
                dots2.add(c1);
                region1.remove(i);
            }
        }
        //get hull of region2
        if(region2.size()>3){
            jItr = 2;
            tempItr=0;
            temp = region2.get(tempItr);
            i = region2.get(1);
            j = region2.get(jItr);
            while(j != region2.get(region2.size()-1)){
                if(area(temp, i ,j)>=0){
                    tempItr++;
                    temp = region2.get(tempItr);
                    i = j;
                    jItr++;
                    j = region2.get(jItr);
                }else{
                    dots3.add(drawdots1(i));
                    region2.remove(i);
                    if(tempItr-1<0){
                        tempItr=0;
                        i=j;
                        j=region2.get(jItr);
                    }else{
                        jItr--;
                        i = region2.get(tempItr);
                        tempItr--;
                        temp = region2.get(tempItr);
                    }
                }
            }
            if(area(temp, i, j)<0){
                dots3.add(drawdots1(i));
                region2.remove(i);
            }
        }
        //get hull of region3
        if(region3.size()>3){
            jItr = 2;
            tempItr=0;
            temp = region3.get(tempItr);
            i = region3.get(1);
            j = region3.get(jItr);
            while(j != region3.get(region3.size()-1)){
                if(area(temp, i ,j)>=0){
                    tempItr++;
                    temp = region3.get(tempItr);
                    i = j;
                    jItr++;
                    j = region3.get(jItr);
                }else{
                    dots4.add(drawdots1(i));
                    region3.remove(i);
                    if(tempItr-1<0){
                        tempItr=0;
                        i=j;
                        j=region3.get(jItr);
                    }else{
                        jItr--;
                        i = region3.get(tempItr);
                        tempItr--;
                        temp = region3.get(tempItr);
                    }
                }
            }
            if(area(temp, i, j)<0){
                dots4.add(drawdots1(i));
                region3.remove(i);
            }
        }
        //get hull of region4
        if(region4.size()>3){
            jItr = 2;
            tempItr=0;
            temp = region4.get(tempItr);
            i = region4.get(1);
            j = region4.get(jItr);
            while(j != region4.get(region4.size()-1)){
                if(area(temp, i ,j)>=0){
                    tempItr++;
                    temp = region4.get(tempItr);
                    i = j;
                    jItr++;
                    j = region4.get(jItr);
                }else{
                    dots5.add(drawdots1(i));
                    region4.remove(i);
                    if(tempItr-1<0){
                        tempItr=0;
                        i=j;
                        j=region4.get(jItr);
                    }else{
                        jItr--;
                        i = region4.get(tempItr);
                        tempItr--;
                        temp = region4.get(tempItr);
                    }
                }
            }
            if(area(temp, i, j)<0){
                dots5.add(drawdots1(i));
                region4.remove(i);
            }
        }
        for(int i=0;i<region1.size();i++){
    points p1 = region1.get(i);
    points p2 = region1.get((i+1)% region1.size());
    lines2.add(drawLine1(p1,p2));
}
for(int i=0;i< region2.size();i++){
    points p1 = region2.get(i);
    points p2 = region2.get((i+1)% region2.size());
    lines3.add(drawLine1(p1,p2));
}
for(int i=0;i<region3.size();i++){
    points p1 = region3.get(i);
    points p2 = region3.get((i+1)% region3.size());
    lines4.add(drawLine1(p1,p2));
}
for(int i=0;i<region4.size();i++){
    points p1 = region4.get(i);
    points p2 = region4.get((i+1)% region4.size());
    lines5.add(drawLine1(p1,p2));
}


        lines2.removeIf(line -> {
            for (Line l : lines) {
                if (areLinesEqual(l, line)) {
                    return true;
                }
            }
            return false;
        });

// Remove elements in lines3 that are in lines
        lines3.removeIf(line -> {
            for (Line l : lines) {
                if (areLinesEqual(l, line)) {
                    return true;
                }
            }
            return false;
        });

// Remove elements in lines4 that are in lines
        lines4.removeIf(line -> {
            for (Line l : lines) {
                if (areLinesEqual(l, line)) {
                    return true;
                }
            }
            return false;
        });

// Remove elements in lines5 that are in lines
        lines5.removeIf(line -> {
            for (Line l : lines) {
                if (areLinesEqual(l, line)) {
                    return true;
                }
            }
            return false;
        });


        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(actionEvent1 -> {
                    for(Line l : lines){
                        pane.getChildren().add(l);
                    }
                });
                PauseTransition pause1 = new PauseTransition(Duration.seconds(1));
                pause1.setOnFinished(actionEvent1 -> {
                    for(Circle c : dots1){
                        pane.getChildren().add(c);
                    }
                });
                PauseTransition pause2 = new PauseTransition(Duration.seconds(1.5));
                pause2.setOnFinished(actionEvent1 -> {
                    Timeline timeline = new Timeline();
                    double delay = 0.0;
                    for (Line l : lines2) {
                    KeyFrame keyFrame = new KeyFrame(Duration.seconds(delay), e -> pane.getChildren().add(l));
                    timeline.getKeyFrames().add(keyFrame);
                    delay += 0.5; // delay in seconds between each line
                    }

                    timeline.play();

//                    for(Line l : lines2){
//                        pane.getChildren().add(l);
//                    }
                });
                PauseTransition pause3 = new PauseTransition(Duration.seconds(2));
                pause3.setOnFinished(actionEvent1 -> {
                    for(Circle c : dots2){
                        pane.getChildren().add(c);
                    }
                });

                PauseTransition pause4 = new PauseTransition(Duration.seconds(2.5));
                pause4.setOnFinished(actionEvent1 -> {
                            Timeline timeline = new Timeline();
                            double delay = 0.0;
                            for (Line l : lines3) {
                                KeyFrame keyFrame = new KeyFrame(Duration.seconds(delay), e -> pane.getChildren().add(l));
                                timeline.getKeyFrames().add(keyFrame);
                                delay += 0.5; // delay in seconds between each line
                            }

                            timeline.play();
                        });
//                    for(Line l : lines3){
//                        pane.getChildren().add(l);
//                    }
//                });
                PauseTransition pause5 = new PauseTransition(Duration.seconds(3));
                pause5.setOnFinished(actionEvent1 -> {
                    for(Circle c : dots3){
                        pane.getChildren().add(c);
                    }
                });

                PauseTransition pause6 = new PauseTransition(Duration.seconds(3.5));
                pause6.setOnFinished(actionEvent1 -> {
                    Timeline timeline = new Timeline();
                    double delay = 0.0;
                    for (Line l : lines4) {
                        KeyFrame keyFrame = new KeyFrame(Duration.seconds(delay), e -> pane.getChildren().add(l));
                        timeline.getKeyFrames().add(keyFrame);
                        delay += 0.5; // delay in seconds between each line
                    }

                    timeline.play();
                });
                PauseTransition pause7 = new PauseTransition(Duration.seconds(4));
                pause7.setOnFinished(actionEvent1 -> {
                    for(Circle c : dots4){
                        pane.getChildren().add(c);
                    }
                });

                PauseTransition pause8 = new PauseTransition(Duration.seconds(4.5));
                pause8.setOnFinished(actionEvent1 -> {
                    Timeline timeline = new Timeline();
                    double delay = 0.0;
                    for (Line l : lines5) {
                        KeyFrame keyFrame = new KeyFrame(Duration.seconds(delay), e -> pane.getChildren().add(l));
                        timeline.getKeyFrames().add(keyFrame);
                        delay += 0.5; // delay in seconds between each line
                    }

                    timeline.play();
                });
                PauseTransition pause9 = new PauseTransition(Duration.seconds(5));
                pause9.setOnFinished(actionEvent1 -> {
                    for(Circle c : dots5){
                        pane.getChildren().add(c);
                    }
                });

                pause.play();
                pause1.play();
                pause2.play();
                pause3.play();
                pause4.play();
                pause5.play();
                pause6.play();
                pause7.play();
                pause8.play();
                pause9.play();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(actionEvent -> {

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(actionEvent1 -> {
                for(Circle c : dots5){
                    pane.getChildren().remove(c);
                }
                for(Line l : lines5){
                    pane.getChildren().remove(l);
                }
                for(Circle c : dots4){
                    pane.getChildren().remove(c);
                }
                for(Line l : lines4){
                    pane.getChildren().remove(l);
                }
                for(Circle c : dots3){
                    pane.getChildren().remove(c);
                }
                for(Line l : lines3){
                    pane.getChildren().remove(l);
                }
                for(Circle c : dots2){
                    pane.getChildren().remove(c);
                }
                for(Line l : lines2){
                    pane.getChildren().remove(l);
                }
                for(Circle c : dots1){
                    pane.getChildren().remove(c);
                }
                for(Line l : lines){
                    pane.getChildren().remove(l);
                }
            });

            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(15));
            pauseTransition.setOnFinished(actionEvent1 -> {
                for(Line l : lines2){
                    l.setStroke(Color.BLUE);
                    pane.getChildren().add(l);
                }
                for(Line l : lines3){
                    l.setStroke(Color.BLUE);
                    pane.getChildren().add(l);
                }
                for(Line l : lines4){
                    l.setStroke(Color.BLUE);
                    pane.getChildren().add(l);
                }
                for(Line l : lines5){
                    l.setStroke(Color.BLUE);
                    pane.getChildren().add(l);
                }


            });
            pause.play();
            pauseTransition.play();
        });
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
//
//        for(int i=0;i<region1.size();i++){
//            points p1 = region1.get(i);
//            points p2 = region1.get((i+1)% region1.size());
//            drawLine(p1,p2);
//        }
//        for(int i=0;i< region2.size();i++){
//            points p1 = region2.get(i);
//            points p2 = region2.get((i+1)% region2.size());
//            drawLine(p1,p2);
//        }
//        for(int i=0;i<region3.size();i++){
//            points p1 = region3.get(i);
//            points p2 = region3.get((i+1)% region3.size());
//            drawLine(p1,p2);
//        }
//        for(int i=0;i<region4.size();i++){
//            points p1 = region4.get(i);
//            points p2 = region4.get((i+1)% region4.size());
//            drawLine(p1,p2);
//        }

//        for (int k = 0; k < region1.size(); k++) {
//            System.out.print(region1.get(k).name + " ");
//        }
//        for (int k = 0; k < region2.size(); k++) {
//            System.out.print(region2.get(k).name + " ");
//        }
//        for (int k = 0; k < region3.size(); k++) {
//            System.out.print(region3.get(k).name + " ");
//        }
//        for (int k = 0; k < region4.size(); k++) {
//            System.out.print(region4.get(k).name + " ");
//        }
    }

    private boolean areLinesEqual(Line line1, Line line2) {
        return (line1.getStartX() == line2.getStartX() && line1.getStartY() == line2.getStartY() &&
                line1.getEndX() == line2.getEndX() && line1.getEndY() == line2.getEndY()) ||
                (line1.getStartX() == line2.getEndX() && line1.getStartY() == line2.getEndY() &&
                        line1.getEndX() == line2.getStartX() && line1.getEndY() == line2.getStartY()) ||
                (line1.getStartX() == line2.getEndX() && line1.getStartY() == line2.getEndY() &&
                        line1.getEndX() == line2.getStartX() && line1.getEndY() == line2.getStartY());
    }
    private double area(points a, points b, points c){
        return ((b.getX()-a.getX())*(c.getY()-a.getY()))-((b.getY()-a.getY())*(c.getX()-a.getX()));
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

    private Line drawLine1(points p1, points p2) {
        System.out.println("Drawing line from (" + p1.getX() + "," + p1.getY() + ") to (" + p2.getX() + "," + p2.getY() + ")");
        gc.setStroke(Color.BLUE);
        gc.setFill(Color.BLUE);
        gc.setLineWidth(2);

        Line l = new Line(p1.getX(),p1.getY(),p2.getX(),p2.getY());
        l.setStroke(Color.RED);
        l.setFill(Color.RED);
        l.setStrokeWidth(2);
        //  gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        return l;
    }
    private Circle drawdots1(points p){
        Circle dots = new Circle(p.getX(),p.getY(),5);
        dots.setStroke(Color.BLUE);
        dots.setFill(Color.BLUE);
        return dots;
    }

    public static void main(String[] args) {
        launch();
    }
}
