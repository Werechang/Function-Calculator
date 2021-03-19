package com.cookieso.function.input;

import com.cookieso.function.Display;
import com.cookieso.function.MyPoint;

public class InputManager {
    public MouseInput mouse;
    public Keyboard keyboard;
    private int initialX, initialY = 0;
    public double sensitivity = 1;

    public InputManager() {
        mouse = new MouseInput();
        keyboard = new Keyboard();
    }

    public void updateInput(MyPoint origin) {
        keyboard.update();
        int x = mouse.getX();
        int y = mouse.getY();
        if(mouse.getButton() == ClickType.LEFT_CLICK) {
            origin.x += (x - initialX) * sensitivity;
            origin.y += (y - initialY) * sensitivity;
        }
        if (mouse.isScrollingDown()) {
            // TODO: Change size: zoom out
        }
        if (mouse.isScrollingUp()) {
            // TODO: Change size: zoom in
        }
        if(keyboard.getSpace()) {
            origin.x = Display.WIDTH/2;
            origin.y = Display.HEIGHT/2;
        }
        mouse.resetScroll();
        initialX = x;
        initialY = y;
    }
}
