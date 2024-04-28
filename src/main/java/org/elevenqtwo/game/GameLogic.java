package org.elevenqtwo.game;

public interface GameLogic {

    void init();

    void input(double deltaTime);

    void update(double deltaTime);

    void render();

    void cleanUp();
}
