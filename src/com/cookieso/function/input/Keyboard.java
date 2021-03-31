package com.cookieso.function.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    private boolean[] keys = new boolean[66568];
    private boolean space, i;

    public void update() {
        space = keys[KeyEvent.VK_SPACE];
        i = keys[KeyEvent.VK_I];
    }
    public boolean getSpace() {
        return this.space;
    }
    public boolean getI() {
        return this.i;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
