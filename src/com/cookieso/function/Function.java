package com.cookieso.function;

import java.awt.*;
import java.util.ArrayList;

import static com.cookieso.function.Display.WIDTH;

public class Function {
    /* origin.x is the x value of the origin relative to the display.
    * point.x is the initial x value in coordinate system.
    * The point is in the display if it´s value is greater than origin.x - point.x. If point.x = -300 and origin.x = 400
    * it means, that the minimum initial x value has to be -400 for the point to be visible. In this case, it is displayed
    * at display.x = 100.
    * The y value is multiplied with -1 because the display uses the inverted y-axis. y = 0 is at the top, HEIGHT is the
    * value for the bottom. Finally, origin.y is added to y for the same reason as origin.x to x.
    * */

    public ArrayList<MyPoint> points = new ArrayList<>();
    public Color color;
    private final double value;

    public Function(Color color, double value) {
        this.color = color;
        this.value = value;
        long startCalc = System.currentTimeMillis();
        calcFunction();
        System.out.println("Finished calculating: " + (System.currentTimeMillis() - startCalc) + " ms");
    }

    public void renderGraph(MyPoint origin, Graphics g) {
        for(MyPoint point : points) {
                if(point.x >= -(origin.x) && point.x <= (WIDTH - origin.x)) {
                    double yValue = (point.y)*-1 + origin.y;
                    g.setColor(color);
                    g.drawRect(Math.round(point.x) + origin.x, (int) Math.round(yValue), 1, 1);
                }
        }
    }

    public void calcFunction() {
        for(double x = -5000; x < 5000; x+=0.005) {
            points.add(new MyPoint((int) x, (int) (-0.002*Math.pow(x, 3) + .2*x*value)  ));
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
