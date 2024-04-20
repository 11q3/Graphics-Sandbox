package org.elevenqtwo.game;

import org.elevenqtwo.core.RenderManager;
import org.elevenqtwo.core.WindowManager;
import org.elevenqtwo.graphics.Entity;
import org.elevenqtwo.graphics.Model;
import org.elevenqtwo.graphics.ObjectLoader;
import org.elevenqtwo.graphics.Texture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements GameLogic {

    private static final float CAMERA_SPEED = 0.1f;

    private final RenderManager renderManager;
    private final ObjectLoader objectLoader;
    private final WindowManager windowManager;
    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    public TestGame() {
        renderManager = new RenderManager();
        windowManager = Launcher.getWindowManager();
        objectLoader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
    }

    @Override
    public void init() throws Exception {
        renderManager.init();


        float[] vertices = new float[] {
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
        };
        float[] textureCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.0f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
                8, 10, 11, 9, 8, 11,
                12, 13, 7, 5, 12, 7,
                14, 15, 6, 4, 14, 6,
                16, 18, 19, 17, 16, 19,
                4, 6, 7, 5, 4, 7,
        };

        Model model = objectLoader.loadModel(vertices, textureCoords, indices);
        model.setTexture(new Texture(objectLoader.loadTexture("src/main/resources/textures/texture2.png")));
        entity = new Entity(model, new Vector3f(0,0,-5), new Vector3f(0,0,0), 1);
    }


    @Override
    public void input() {
        cameraInc.set(0,0,0);
        if(windowManager.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if(windowManager.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if(windowManager.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = 1;
        if(windowManager.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = -1;

        if(windowManager.isKeyPressed(GLFW.GLFW_KEY_Z))
            cameraInc.y = -1;
        if(windowManager.isKeyPressed(GLFW.GLFW_KEY_X))
            cameraInc.y = 1;
    }

    @Override
    public void update() {
        camera.movePosition(cameraInc.x * CAMERA_SPEED, cameraInc.y * CAMERA_SPEED, cameraInc.z * CAMERA_SPEED);

        entity.incRotation(0.0f, 0.5f, 0.0f);
    }

    @Override
    public void render() {
        if( windowManager.isResize()) {
            GL11.glViewport(0,0,windowManager.getWidth(), windowManager.getHeight());
            windowManager.setResize(true);
        }

        windowManager.setClearColor(256.0f, 256.0f, 256.0f, 0.0f);
        renderManager.render(entity, camera);
    }

    @Override
    public void cleanUp() {
        renderManager.cleanUp();
        objectLoader.cleanUp();
    }
}
