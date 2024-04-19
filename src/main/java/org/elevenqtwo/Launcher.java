package org.elevenqtwo;

import org.elevenqtwo.util.Constants;

public class Launcher {

    public static WindowManager windowManager;
    private static TestGame game;

    public static void main(String[] args) {

        windowManager = new WindowManager(Constants.TITLE, 1600, 800, false);
        game = new TestGame();
        EngineManager engineManager = new EngineManager();
        try {
            engineManager.startEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static WindowManager getWindowManager() {
        return windowManager;
    }

    public static TestGame getGameLogic() {
        return game;
    }
}