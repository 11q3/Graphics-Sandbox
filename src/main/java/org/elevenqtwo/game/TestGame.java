package org.elevenqtwo.game;

import org.elevenqtwo.core.RenderManager;
import org.elevenqtwo.core.WindowManager;
import org.elevenqtwo.graphics.Entity;
import org.elevenqtwo.graphics.Model;
import org.elevenqtwo.graphics.ObjectLoader;
import org.elevenqtwo.graphics.Texture;
import org.elevenqtwo.util.Constants;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements GameLogic {

    private final RenderManager renderManager;
    private final ObjectLoader objectLoader;
    private final WindowManager windowManager;
    private MouseInput mouseInput;
    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    public TestGame() {
        renderManager = new RenderManager();
        windowManager = Launcher.getWindowManager();
        objectLoader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        mouseInput = new MouseInput(windowManager);
    }

    @Override
    public void init() {
        float[] vertices = new float[]{
                // Front face
                -0.5f, 0.5f, 0.5f, // top left
                -0.5f, -0.5f, 0.5f, // bottom left
                0.5f, -0.5f, 0.5f, // bottom right
                0.5f, 0.5f, 0.5f, // top right

                // Back face
                -0.5f, 0.5f, -0.5f, // top left
                -0.5f, -0.5f, -0.5f, // bottom left
                0.5f, -0.5f, -0.5f, // bottom right
                0.5f, 0.5f, -0.5f, // top right

                // Left face
                -0.5f, 0.5f, 0.5f, // top left
                -0.5f, -0.5f, 0.5f, // bottom left
                -0.5f, -0.5f, -0.5f, // bottom right
                -0.5f, 0.5f, -0.5f, // top right

                // Right face
                0.5f, 0.5f, 0.5f, // top left
                0.5f, -0.5f, 0.5f, // bottom left
                0.5f, -0.5f, -0.5f, // bottom right
                0.5f, 0.5f, -0.5f, // top right

                // Top face
                -0.5f, 0.5f, 0.5f, // top left
                0.5f, 0.5f, 0.5f, // top right
                0.5f, 0.5f, -0.5f, // bottom right
                -0.5f, 0.5f, -0.5f, // bottom left

                // Bottom face
                -0.5f, -0.5f, 0.5f, // top left
                0.5f, -0.5f, 0.5f, // top right
                0.5f, -0.5f, -0.5f, // bottom right
                -0.5f, -0.5f, -0.5f  // bottom left
        };
        float[] textureCoords = new float[]{
                // Front face
                0, 0, // top left
                0, 1, // bottom left
                1, 1, // bottom right
                1, 0, // top right

                // Back face
                0, 0,
                0, 1,
                1, 1,
                1, 0,

                // Left face
                0, 0,
                0, 1,
                1, 1,
                1, 0,

                // Right face
                0, 0,
                0, 1,
                1, 1,
                1, 0,

                // Top face
                0, 0,
                1, 0,
                1, 1,
                0, 1,

                // Bottom face
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };
        int[] indices = new int[]{
                0, 1, 3,
                3, 1, 2,
                4, 5, 7,
                7, 5, 6,
                8, 9, 11,
                11, 9, 10,
                12, 13, 15,
                15, 13, 14,
                16, 17, 19,
                19, 17, 18,
                20, 21, 23,
                23, 21, 22,
        };


        try {
            renderManager.init();
            Model model = objectLoader.loadModel(vertices, textureCoords, indices);
            model.setTexture(new Texture(objectLoader.loadTexture("src/main/resources/textures/img.png")));

            entity = new Entity(model, new Vector3f(0, 0, -5), new Vector3f(0, 0, 0), 1);
            GLFW.glfwSetInputMode(Launcher.windowManager.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }


    @Override
    public void input() {
        mouseInput.update();

        cameraInc.set(0, 0, 0);
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            cameraInc.y = -1;
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_SPACE))
            cameraInc.y = 1;
    }

    @Override
    public void update() {
        camera.movePosition(cameraInc.x * Constants.CAMERA_SPEED,
                cameraInc.y * Constants.CAMERA_SPEED,
                cameraInc.z * Constants.CAMERA_SPEED);

        float rotX = mouseInput.getDX() * Constants.CAMERA_SENSITIVITY;
        float rotY = mouseInput.getDY() * Constants.CAMERA_SENSITIVITY;
        camera.moveRotation(rotX, rotY, 0);

        entity.incRotation(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void render() {
        if (windowManager.isResize()) {
            GL11.glViewport(0, 0, windowManager.getWidth(), windowManager.getHeight());
            windowManager.setResize(false);
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
