package org.elevenqtwo;

import org.elevenqtwo.model.Model;
import org.elevenqtwo.model.Texture;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements Logic{

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderManager;
    private final ObjectLoader objectLoader;
    private final WindowManager windowManager;

    private Model model;

    public TestGame() {
        renderManager = new RenderManager();
        windowManager = Launcher.getWindowManager();
        objectLoader = new ObjectLoader();
    }

    @Override
    public void init() throws Exception {
        renderManager.init();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        float[] textureCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        model = objectLoader.loadModel(vertices, textureCoords, indices);
        model.setTexture(new Texture(objectLoader.loadTexture("src/main/resources/textures/texture2.png")));
    }

    @Override
    public void input() {
        if(windowManager.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            direction = 1;
        } else if (windowManager.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            direction = -1;
        } else direction = 0;
    }

    @Override
    public void update() {
        color += direction * 0.05f;
        if(color > 1 ) {
            color = 1.0f;
        } else if (color <= 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render() {
        if( windowManager.isResize()) {
            GL11.glViewport(0,0,windowManager.getWidth(), windowManager.getHeight());
            windowManager.setResize(true);
        }

        windowManager.setClearColor(color, color, color, 0.0f);
        renderManager.render(model);
    }

    @Override
    public void cleanUp() {
        renderManager.cleanUp();
        objectLoader.cleanUp();
    }
}
