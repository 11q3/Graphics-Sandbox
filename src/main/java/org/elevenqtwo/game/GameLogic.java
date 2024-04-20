package org.elevenqtwo.game;

public interface GameLogic {

    void init() throws Exception;

    void input();

    void update();
    void render();
    void cleanUp();
}
