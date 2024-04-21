package org.elevenqtwo.game;

public interface GameLogic {

    void init() throws Exception;

    void input();

    void update(MouseInput mouseInput);
    void render();
    void cleanUp();
}
