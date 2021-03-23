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
    private Boolean isFirstCalc = true;
    public ArrayList<Double> zeros = new ArrayList<>();
    public final String equation;

    public Function(Color color, String equation) {
        this.color = color;
        this.equation = equation;
        long startCalc = System.currentTimeMillis();
        calcFunction(-1000, 1000, 0.005);
        calcRoot(0.0000001);
        System.out.println("Finished calculating: " + (System.currentTimeMillis() - startCalc) + " ms");
    }

    public Function(String equation) {
        this.color = Color.MAGENTA;
        this.equation = equation;
        long startCalc = System.currentTimeMillis();
        calcFunction(-2000, 2000, 0.005);
        calcRoot(0.0000001);
        System.out.println("Finished calculating: " + (System.currentTimeMillis() - startCalc) + " ms");
    }

    public void renderGraph(MyPoint origin, Graphics g) {
        try {
            for(MyPoint point : points) {
                if(point.x*scale >= -(origin.x) && point.x*scale <= (WIDTH - origin.x) && (point.x % (0.5/scale) >= -0.01) && (point.x % (0.5/scale) <= 0.01)) {
                    double yValue = (point.y)*-1*scale + origin.y;
                    g.setColor(color);
                    g.drawRect((int) Math.round((point.x*scale + origin.x)), (int) Math.round(yValue), 1, 1);
                }
            }
        } catch (ConcurrentModificationException ignored) {}
    }

    public void calcFunction(double start, double end, double increase) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        // Calculate f(x) of each point, add those into the ArrayList
        if (isFirstCalc) {
            try {
                for(double x = start; x < end; x+=increase) {
                    engine.put("x", x);
                    points.add(new MyPoint(x, new Double(engine.eval(equation).toString())));
                }
                isFirstCalc = false;
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void updateFunctionBuffer(int buffer) {
        // Remove points outside of a specific range (display + buffer)
        points.removeIf(point -> origin.x + point.x > WIDTH + buffer || origin.x - point.x < -buffer);
        // calcFunction(-origin.x - 100, -origin.x + WIDTH + 100, 0.005);
    }

    private void calcRoot(double epsilon) {
        // calculating the root of this function with Newton´s method: x_n+1 = x_n - f(x)/f'(x);
    }

    public void translateEquation(String f) {

    }
}
