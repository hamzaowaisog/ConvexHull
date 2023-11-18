package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PolygonTest {
    @Test
    public void testIsPointInsidePolygon() {
        QuickEliminationConvexHull app = new QuickEliminationConvexHull();

        ArrayList<points> polygon = new ArrayList<>();
        polygon.add(new points(0, 0));
        polygon.add(new points(10, 0));
        polygon.add(new points(10, 10));
        polygon.add(new points(0, 10));

        // Test with a point inside the polygon
        points insidePoint = new points(5, 5);
        assertTrue(app.isPointInsidePolygon(insidePoint, polygon));

        // Test with a point outside the polygon
        points outsidePoint = new points(15, 15);
        assertFalse(app.isPointInsidePolygon(outsidePoint, polygon));

        // Test with a point on the boundary of the polygon
        points boundaryPoint = new points(10, 5);
        assertTrue(app.isPointInsidePolygon(boundaryPoint, polygon));
    }
}
  
