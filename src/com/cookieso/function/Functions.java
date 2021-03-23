package com.cookieso.function;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Functions {
    public ArrayList<Function> functions = new ArrayList<>();

    public Functions(Function... functions) {
        Collections.addAll(this.functions, functions);
    }

    public void renderFunctions(MyPoint origin, Graphics g) {
        for (Function function : this.functions) {
            function.renderGraph(origin, g);
        }
    }

    public void addFunction(Function function) {
        functions.add(function);
    }

    public void updateFunctionBuffers(int maxDistance) {
        for (Function function : functions) {
            function.updateFunctionBuffer(maxDistance);
        }
    }
}
