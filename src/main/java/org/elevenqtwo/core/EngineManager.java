package org.elevenqtwo.core;

import org.elevenqtwo.game.Launcher;
import org.elevenqtwo.game.GameLogic;
import org.elevenqtwo.game.MouseInput;
import org.elevenqtwo.util.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECOND = 1000000000L;
    public static float TARGET_FRAMERATE = 6000;
    private static int fps;
    private static final float frameTime = 1.0f / TARGET_FRAMERATE;
    private boolean isRunning;

    private WindowManager windowManager;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private GameLogic gameLogic;

    private long lastTickTime = System.nanoTime();
    private int ticks = 0;
    private final int maxTicks = 120;

    private void init()  {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        windowManager = Launcher.getWindowManager();
        gameLogic = Launcher.getGameLogic();
        mouseInput = new MouseInput(windowManager);
        windowManager.init();
        gameLogic.init();
        mouseInput.init();
    }

    public void startEngine() throws Exception {
        init();
        if (isRunning) {
            return;
        }
        run();
    }

    private void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
    }

    private void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            unprocessedTime += passedTime / (double) NANOSECOND;

            frameCounter += passedTime;

            input();

            while (unprocessedTime > frameTime) {
                render = true;
                unprocessedTime -= frameTime;

                if (windowManager.windowShouldClose()) {
                    stop();
                }

                // tick logic
                long currentTime = System.nanoTime();
                long tickTime = currentTime - lastTickTime;
                if (tickTime >= NANOSECOND / maxTicks) {
                    lastTickTime = currentTime;
                    ticks++;
                    update();
                }
            }

            if (render) {
                render();
                frames++;
            }

            lastTime = startTime;

            if (frameCounter >= NANOSECOND) {
                setFps(frames);
                windowManager.setTitle(Constants.TITLE + " " + getFps());
                frames = 0;
                frameCounter = 0;
            }
        }
        cleanUp();
    }

    private void setFps(int fps) {
        EngineManager.fps = fps;
    }

    private void render() {
        gameLogic.render();
        windowManager.updateWindow();
    }

    private void input() {
        gameLogic.input();
    }

    private void update() {
        gameLogic.update();
    }

    private void cleanUp() {
        windowManager.cleanUp();
        gameLogic.cleanUp();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }


}