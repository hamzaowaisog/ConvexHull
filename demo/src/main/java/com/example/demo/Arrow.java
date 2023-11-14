package com.example.demo;

import javafx.scene.shape.Line;

public class Arrow extends Line {
    public Arrow(double x, double y, double length, boolean clockwise, double endX, double endY) {
        setStartX(x);
        setStartY(y);
        double angle = Math.atan2(endY - y, endX - x);

        if (!clockwise) {
            angle += Math.PI * 4;
        }

        endX = x + length * Math.cos(angle);
        endY = y + length * Math.sin(angle);

        setEndX(endX);
        setEndY(endY);

        setStroke(javafx.scene.paint.Color.GREEN);
    }
}
