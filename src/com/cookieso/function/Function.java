package com.cookieso.function;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.cookieso.function.Display.*;

public class Function {
    /* origin.x is the x value of the origin relative to the display.
    * point.x is the initial x value in the coordinate system.
    * The point is in the display if it´s value is greater than origin.x - point.x. If point.x = -300 and origin.x = 400
    * it means, that the minimum initial x value has to be -400 for the point to be visible. In this case, it is displayed
    * at display.x = 100.
    * The y value is multiplied with -1 because the display uses the inverted y-axis. y = 0 is at the top, HEIGHT is the
    * value for the bottom. Finally, origin.y is added to y for the same reason as origin.x to x.
    * The last part of the if statement is for performance. With large scales (actually a small scale number) it paints
    * less points.
    * */

    // TODO: Fix scaling: currently scales to center

    public ArrayList<MyPoint> points = new ArrayList<>();
    public Color color;
    private final double value;

    public Function(Color color, double value) {
        this.color = color;
        this.value = value;
        long startCalc = System.currentTimeMillis();
        calcFunction(-1000, 1000, 0.005);
        System.out.println("Finished calculating: " + (System.currentTimeMillis() - startCalc) + " ms");
    }

    public void renderGraph(MyPoint origin, Graphics g) {
        try {
            for(MyPoint point : points) {
                updateFunctionBuffer(point, 4000);
                if(point.x*scale >= -(origin.x) && point.x*scale <= (WIDTH - origin.x) && (point.x % (0.5/scale) >= -0.01) && (point.x % (0.5/scale) <= 0.01)) {
                    double yValue = (point.y)*-1*scale + origin.y;
                    g.setColor(color);
                    g.drawRect((int) Math.round((point.x*scale + origin.x)), (int) Math.round(yValue), 1, 1);
                }
            }
        } catch (ConcurrentModificationException ignored) { }
    }

    public void calcFunction(int start, int end, double increase) {
        // Placeholder String for testing
        String equation = "3*x + 10";

        // calculate f(x) of each point, add those into the ArrayList
        for(double x = start; x < end; x+=increase) {
            points.add(new MyPoint(x, value*0.1*x + value) );
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void updateFunctionBuffer(MyPoint point, int buffer) {
        // Remove points outside of a specific range (display + buffer)
        if(origin.x + point.x*scale > WIDTH + buffer) {
            points.remove(point);
        }
        else if(origin.x - point.x*scale < -buffer) {
            points.remove(point);
        }
    }
}
