package com.cookieso.function;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.cookieso.function.Display.*;

public class Function {
    /*
    * origin.x is the x value of the origin relative to the display.
    * point.x is the initial x value in the coordinate system.
    * The point is in the display if it´s value is greater than origin.x - point.x. If point.x = -300 and origin.x = 400
    * it means, that the minimum initial x value has to be -400 for the point to be visible. In this case, it is displayed
    * at display.x = 100.
    * The y value is multiplied with -1 because the display uses the inverted y-axis. y = 0 is at the top, HEIGHT is the
    * value for the bottom. Finally, origin.y is added to y for the same reason as origin.x to x.
    * The last part of the if statement is for performance. With large scales (actually a small scale number) it draws
    * less points.
    * */

    // TODO: Fix scaling: currently scales to center

    public ArrayList<MyPoint> points = new ArrayList<>();
    public Color color;
    public ArrayList<Double> zeros = new ArrayList<>();
    public final String equation;
    public static int rectHeightExponent = 0;

    public Function(Color color, String equation) {
        this.color = color;
        this.equation = equation;
        long startCalc = System.currentTimeMillis();
        calcFunction(-1000, 1000, 0.04);
        calcRoot(0.0000001);
        System.out.println("Finished calculating: " + (System.currentTimeMillis() - startCalc) + " ms");
    }

    public Function(String equation) {
        this.color = Color.MAGENTA;
        this.equation = equation;
        long startCalc = System.currentTimeMillis();
        calcFunction(-2000, 2000, 0.04);
        calcRoot(0.0000001);
        System.out.println("Finished calculating: " + (System.currentTimeMillis() - startCalc) + " ms");
    }

    public void renderGraph(MyPoint origin, Graphics g) {
        MyPoint lastPoint = null;
        try {
            for(MyPoint point : points) {
                // TODO: The visibility of the point should depend on f'(point.x) --------------------here---------------------------------here----
                if(point.x*scale >= -(origin.x) && point.x*scale <= (WIDTH - origin.x) && (point.x % (0.01/scale) >= -0.01) && (point.x % (0.01/scale) <= 0.01)) {
                    double yValue = (point.y)*-1*scale + origin.y;
                    int rectHeight = rectHeightExponent < 1 ? 1 : (int) Math.round(point.y*scale);
                    if (lastPoint != null && rectHeightExponent != 1) {
                        double diff = point.y - lastPoint.y;
                        if (diff == 0) {
                            rectHeight = 1;
                        } else if (diff < 0) {
                            rectHeight = (int) Math.floor(diff);
                        } else {
                            rectHeight = (int) Math.ceil(diff);
                        }
                        rectHeight = Math.abs(rectHeight);
                    }
                    g.setColor(color);
                    g.drawRect((int) Math.round((point.x*scale + origin.x)), (int) Math.round(yValue), 1, rectHeight);
                }
                lastPoint = point;
            }
        } catch (ConcurrentModificationException ignored) {}
    }

    public void calcFunction(double start, double end, double increase) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        // Calculate f(x) of each point, add those into the ArrayList
        try {
            for(double x = start; x < end; x+=increase) {
                // Currently I´m using a slow ScriptEngine. Performance updates are coming in the future
                engine.put("x", x);
                points.add(new MyPoint(x, new Double(engine.eval(equation).toString())));
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void updateFunctionBuffer(int buffer) {
        // Remove points outside of a specific range (display + buffer)
        points.removeIf(point -> origin.x + point.x > WIDTH + buffer || origin.x - point.x < -buffer);
        points.sort((o1, o2) -> {
            if (o1.x == o2.x) {
                return 0;
            }
            return o1.x < o2.x ? 1 : -1;
        });
    }

    private void calcRoot(double epsilon) {
        // calculating the root of this function with Newton´s method: x_n+1 = x_n - f(x)/f'(x);
        /*
        * double x = 0;
        * while (f(x) < epsilon) {
        * x -= f(x)/f'(x);
        * }
        *
        * */
    }
}
