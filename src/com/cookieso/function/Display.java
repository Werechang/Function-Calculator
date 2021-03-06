package com.cookieso.function;

import com.cookieso.function.input.InputManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;


public class Display extends Canvas implements Runnable{
    private static final long serialVersionUID = 1L;

    // Input settings are in InputManager
    public static int HEIGHT = 600;
    public static int WIDTH = 800;
    public static final String TITLE = "Function Calculator";
    public static final int FPS = 60;

    public static double scale = 10;
    public static final Color BACKGROUND_COLOR = new Color(0x26292c);
    public static final Color AXIS_COLOR = new Color(0xA1A1A1);
    public static final Color EXTRA_AXIS_COLOR = new Color(0x256E74);
    public static final Color SMALL_AXIS_COLOR = new Color(0x404040);

    private final JFrame frame;

    private Thread thread;
    private boolean running = false;

    private final InputManager inputManager;

    private static Functions functions;
    public static MyPoint origin;

    public Display() {
        frame = new JFrame();
        inputManager = new InputManager();

        this.addMouseListener(inputManager.mouse);
        this.addMouseMotionListener(inputManager.mouse);
        this.addMouseWheelListener(inputManager.mouse);
        this.addKeyListener(inputManager.keyboard);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Display display = new Display();

        display.frame.add(display);
        display.frame.pack();
        display.frame.setSize(WIDTH, HEIGHT);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(true);
        display.frame.setTitle(Display.TITLE);
        display.frame.setMinimumSize(new Dimension(300, 250));
        display.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        display.frame.setVisible(true);

        origin = new MyPoint(WIDTH/2.0, HEIGHT/2.0);

        Function function1 = new Function(Color.RED, "0.3*(x*x)+3");
        Function function2 = new Function(Color.BLUE, "3*x - 10");
        functions = new Functions(function1, function2);

        System.out.println("Exiting main after " + (System.currentTimeMillis() -  start) + " ms");
        display.start();
    }

    private synchronized void start() {
        if(running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop(){
        if(!running) return;
        running = false;
        System.out.println("Stopping...");
        try {
            thread.join();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / FPS;
        double delta = 0;
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += ((now - lastTime) / ns);
            lastTime = now;

            HEIGHT = frame.getHeight();
            WIDTH = frame.getWidth();

            while (delta >= 1) {
                update();
                delta--;
                render();
                frames++;
            }

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(TITLE + " | " + frames + " fps");
                frames = 0;
            }
        }
    }

    private void update() {
        inputManager.updateInput(origin);
        functions.updateFunctionBuffers(4000);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        // Background
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, WIDTH * 3, HEIGHT * 3);

        renderCoordinateSystem(g);

        // Draw functions
        functions.renderFunctions(origin, g);
        g.dispose();
        bs.show();
    }

    private void renderCoordinateSystem(Graphics g) {
        // Paint small grid
        drawAxis(1, g, SMALL_AXIS_COLOR, 5);
        // Paint large grid
        drawAxis(10, g, EXTRA_AXIS_COLOR, 1);

        g.setColor(AXIS_COLOR);
        // Main axis
        g.fillRect(0, (int) origin.y, WIDTH*4, 2);
        g.fillRect((int) origin.x,0 , 2, HEIGHT*4);
    }

    private void drawAxis(int gridSize, Graphics g, Color color, int maxScale) {
        if (scale >= maxScale) {
            g.setColor(color);
            for (double i = origin.x%(scale*gridSize); i <= WIDTH; i+=gridSize*scale) {
                g.fillRect((int) i, 0, 1, HEIGHT*3);
            }
            for (double i = origin.y%(scale*gridSize); i <= HEIGHT; i+=gridSize*scale) {
                g.fillRect(0, (int) i, WIDTH*3, 1);
            }
        }
    }
}
