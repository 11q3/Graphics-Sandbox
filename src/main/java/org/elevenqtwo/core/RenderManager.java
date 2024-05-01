package org.elevenqtwo.core;

import org.elevenqtwo.game.Camera;
import org.elevenqtwo.game.Launcher;
import org.elevenqtwo.graphics.Entity;
import org.elevenqtwo.util.Constants;
import org.elevenqtwo.util.ResourceLoader;
import org.elevenqtwo.util.Transformation;
import org.lwjgl.opengl.*;

public class RenderManager {

    private final WindowManager windowManager;
    public ShaderManager shaderManager;

    public RenderManager() {
        windowManager = Launcher.getWindowManager();
    }

    public void init() throws Exception {
        shaderManager = new ShaderManager();
        shaderManager.createVertexShader(ResourceLoader.loadResource("/shaders/vertex.vert"));
        shaderManager.createFragmentShader(ResourceLoader.loadResource("/shaders/fragment.frag"));
        shaderManager.link();
        shaderManager.createUniform("textureSampler");
        shaderManager.createUniform("transformationMatrix");
        shaderManager.createUniform("projectionMatrix");
        shaderManager.createUniform("viewMatrix");
        shaderManager.createUniform("ambientLight");
        shaderManager.createMaterialUniform("material");
    }

    public void render(Entity entity, Camera camera) {
        clear();

        if(windowManager.isResize()) {
            GL11.glViewport(0,0,windowManager.getHeight(), windowManager.getWidth());
            windowManager.setResize(false);
        }

        shaderManager.bind();
        shaderManager.setUniform("textureSampler", 0);
        shaderManager.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
        shaderManager.setUniform("projectionMatrix", windowManager.updateProjectionMatrix());
        shaderManager.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        shaderManager.setUniform("material", entity.getModel().getMaterial());
        shaderManager.setUniform("ambientLight", Constants.AMBIENT_LIGHT);

        GL30.glBindVertexArray(entity.getModel().getId());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getId());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        shaderManager.unbind();
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanUp() {
        shaderManager.cleanUp();
    }
}
