package org.elevenqtwo;

import org.elevenqtwo.util.Consts;

public class Launcher {

    public static WindowManager windowManager;
    private static EngineManager engineManager;

    public static void main(String[] args) {

        windowManager = new WindowManager(Consts.TITLE, 1600, 800, false);
        engineManager = new EngineManager();

        try {
            engineManager.startEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static WindowManager getWindowManager() {
        return windowManager;
    }
}