package org.elevenqtwo.game;

import org.elevenqtwo.core.EngineManager;
import org.elevenqtwo.core.WindowManager;
import org.elevenqtwo.util.Constants;

import java.util.ArrayList;

public class Launcher {

    public static WindowManager windowManager;
    private static TestGame game;

    public static void main(String[] args) {

        windowManager = new WindowManager(Constants.TITLE, 3840, 2560, false);
        game = new TestGame();
        EngineManager engineManager = new EngineManager();
        try {
            engineManager.startEngine();
        } catch (Exception e) {
            System.err.println("Unable to launch engine: " + e.getMessage());
        }
    }

    public static WindowManager getWindowManager() {
        return windowManager;
    }

    public static TestGame getGameLogic() {
        return game;
    }
}