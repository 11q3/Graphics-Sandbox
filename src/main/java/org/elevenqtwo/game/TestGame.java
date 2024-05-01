package org.elevenqtwo.game;

import org.elevenqtwo.core.RenderManager;
import org.elevenqtwo.core.WindowManager;
import org.elevenqtwo.graphics.Entity;
import org.elevenqtwo.graphics.Model;
import org.elevenqtwo.graphics.ObjectLoader;
import org.elevenqtwo.graphics.Texture;
import org.elevenqtwo.util.Constants;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.IOException;

public class TestGame implements GameLogic {

    private final RenderManager renderManager;
    private final ObjectLoader objectLoader;
    private final WindowManager windowManager;
    private MouseInput mouseInput;
    private Entity entity;
    private Camera camera;

    Vector3f cameraIncrement;

    public TestGame() throws IOException {
        renderManager = new RenderManager();
        windowManager = Launcher.getWindowManager();
        objectLoader = new ObjectLoader();
        camera = new Camera();
        cameraIncrement = new Vector3f(0, 0, 0);
        mouseInput = new MouseInput(windowManager);
    }

    @Override
    public void init() {
        objectLoader.loadOBJModel("")
        try {
            GLFW.glfwSetInputMode(Launcher.windowManager.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

            renderManager.init();
            Model model = objectLoader.loadOBJModel("src/main/resources/models/floppacube/FloppaCube.obj",
                    "src/main/resources/models/floppacube/FloppaCube.mtl");

            GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            GL30.glBindVertexArray(0);


            entity = new Entity(model, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void input() {
        mouseInput.update();

        double mouseX = mouseInput.getDX();
        double mouseY = mouseInput.getDY();

        camera.moveRotation(
                (float) (mouseX * Constants.CAMERA_SENSITIVITY),
                (float) (mouseY * Constants.CAMERA_SENSITIVITY),
                0);

        cameraIncrement.set(0, 0, 0);
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraIncrement.z = (Constants.CAMERA_SPEED * -1);
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraIncrement.z = (Constants.CAMERA_SPEED * 1);

        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraIncrement.x = (Constants.CAMERA_SPEED * -1);
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraIncrement.x = (Constants.CAMERA_SPEED);

        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            cameraIncrement.y =(Constants.CAMERA_SPEED * -1);
        if (windowManager.isKeyPressed(GLFW.GLFW_KEY_SPACE))
            cameraIncrement.y = (Constants.CAMERA_SPEED * 1);
    }

    @Override
    public void update() {
        camera.movePosition(cameraIncrement.x * Constants.CAMERA_SPEED,
                cameraIncrement.y * Constants.CAMERA_SPEED,
                cameraIncrement.z * Constants.CAMERA_SPEED);

        entity.moveRotation(0,0,0);
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
        //objectLoader.cleanup();
    }
}
