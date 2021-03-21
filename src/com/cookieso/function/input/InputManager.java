package com.cookieso.function.input;

import com.cookieso.function.Display;
import com.cookieso.function.MyPoint;

public class InputManager {
    public MouseInput mouse;
    public Keyboard keyboard;
    private int initialX, initialY = 0;
    public final double SENSITIVITY = 1;
    public final double ZOOM_SENSITIVITY = 1.2;
    public final double MAX_SCALE = 0.5;
    public final double MIN_SCALE = 400;

    public InputManager() {
        mouse = new MouseInput();
        keyboard = new Keyboard();
    }

    public void updateInput(MyPoint origin) {
        keyboard.update();
        int x = mouse.getX();
        int y = mouse.getY();
        if(mouse.getButton() == ClickType.LEFT_CLICK) {
            origin.x += (x - initialX) * SENSITIVITY;
            origin.y += (y - initialY) * SENSITIVITY;
        }
        if (mouse.isScrollingDown() && Display.scale >= MAX_SCALE) {
            Display.scale /= ZOOM_SENSITIVITY;
        }
        if (mouse.isScrollingUp() && Display.scale <= MIN_SCALE) {
            Display.scale *= ZOOM_SENSITIVITY;
        }
        if(keyboard.getSpace()) {
            origin.x = Display.WIDTH/2.0;
            origin.y = Display.HEIGHT/2.0;
            Display.scale = 10;
        }
        mouse.resetScroll();
        initialX = x;
        initialY = y;
    }
}
