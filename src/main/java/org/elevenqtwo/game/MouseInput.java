package org.elevenqtwo.game;

import org.elevenqtwo.core.WindowManager;
import org.elevenqtwo.util.Constants;


public class MouseInput {
    private final WindowManager windowManager;
    private double lastX, lastY;
    private double dx, dy;

    public MouseInput(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public void init() {
        lastX = windowManager.getMouseX();
        lastY = windowManager.getMouseY();
    }

    public void update() {
        double x = windowManager.getMouseX();
        double y = windowManager.getMouseY();
        dy = (x - lastX) * Constants.CAMERA_SENSITIVITY;
        dx = (y - lastY) * Constants.CAMERA_SENSITIVITY;
        lastX = x;
        lastY = y;
    }

    public float getDX() {
        return (float) dx;
    }

    public float getDY() {
        return (float) dy;
    }
}