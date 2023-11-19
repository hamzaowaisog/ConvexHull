package com.example.demo;

public class points {
    public boolean isOnHull;
    public boolean flag;
    private double x;
    private double y;

    public points() {
    }

    public points(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
