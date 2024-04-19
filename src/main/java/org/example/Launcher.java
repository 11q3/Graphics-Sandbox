package org.example;

public class Launcher {


    public static void main(String[] args) {
        WindowManager windowManager = new WindowManager("graphic", 800, 600, false);
        windowManager.init();

        while(!windowManager.windowShouldClose()) {
            windowManager.update();
        }

        windowManager.cleanUp();
    }
}