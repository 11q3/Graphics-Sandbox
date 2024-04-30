package org.elevenqtwo.core;

import org.elevenqtwo.game.Launcher;
import org.elevenqtwo.game.GameLogic;
import org.elevenqtwo.game.MouseInput;
import org.elevenqtwo.util.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    private static int FPS;
    private boolean isRunning;

    private WindowManager windowManager;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private GameLogic gameLogic;

    private long lastTickTime = System.nanoTime();
    private static final int MAX_TICKS = 120;

    private void init() {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        windowManager = Launcher.getWindowManager();
        gameLogic = Launcher.getGameLogic();
        mouseInput = new MouseInput(windowManager);
        windowManager.init();
        gameLogic.init();
        mouseInput.init();
    }

    public void startEngine() {
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
            unprocessedTime += passedTime / (double) Constants.NANOSECOND;
            frameCounter += passedTime;

            if (frameCounter >= Constants.NANOSECOND) {
                setFps(frames);
                windowManager.setTitle(Constants.TITLE + " " + getFPS());
                frames = 0;
                frameCounter = 0;
            }

            input();

            while (unprocessedTime > Constants.FRAMETIME) {
                render = true;
                unprocessedTime -= Constants.FRAMETIME;

                if (windowManager.windowShouldClose()) stop();

                long currentTime = System.nanoTime();
                long tickTime = currentTime - lastTickTime;

                if (tickTime >= Constants.NANOSECOND / MAX_TICKS) {
                    lastTickTime = currentTime;
                    update();
                }
            }

            if (render) {
                render();
                frames++;
            }

            lastTime = startTime;


        }
        cleanUp();
    }

    private void setFps(int fps) {
        EngineManager.FPS = fps;
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

    public static int getFPS() {
        return FPS;
    }
}