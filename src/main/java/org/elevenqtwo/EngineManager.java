package org.elevenqtwo;

import org.elevenqtwo.util.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;
    private static int fps;
    private static float frameTime = 1.0f / FRAMERATE;
    private boolean isRunning;

    private WindowManager windowManager;
    private GLFWErrorCallback errorCallback;
    private Logic gameLogic;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        windowManager = Launcher.getWindowManager();
        gameLogic = Launcher.getGameLogic();
        windowManager.init();
        gameLogic.init();
    }

    public void startEngine() throws Exception {
        init();
        if(isRunning) {
            return;
        }
        run();
    }

    private void stop() {
        if(!isRunning) {
            return;
        } isRunning = false;
    }

    public void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime  = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            input();

            while(unprocessedTime > frameTime) {
                render = true;
                unprocessedTime -= frameTime;

                if(windowManager.windowShouldClose()) {
                    stop();
                }

                if(frameCounter >= NANOSECOND) {
                    setFps(frames);
                    windowManager.setTitle(Constants.TITLE + " " + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render) {
                update();
                render();
                frames++;
            }
        }
        cleanUp();
    }

    private void setFps(int fps) {
        EngineManager.fps = fps;
    }

    private void input() {
        gameLogic.input();
    }

    private void render() {
        gameLogic.render();
        windowManager.updateWindow();
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

