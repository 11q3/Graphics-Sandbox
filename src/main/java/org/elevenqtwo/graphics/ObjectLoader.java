package org.elevenqtwo.graphics;

import org.elevenqtwo.graphics.Model;

import org.elevenqtwo.util.BufferAllocator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {
    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();
    private final List<Integer> textures = new ArrayList<>();

    public Model loadModel(float[] vertices, float[] textureCoords, int[] indices) {
        int id = createVAD();
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0, vertices);
        storeDataInAttributeList(1, textureCoords);
        unbind();
        return new Model(id, indices.length);
    }

    public int loadTexture(String fileName) throws Exception {
        int width;
        int height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(fileName, w, h, c, 4);
            if (buffer == null) {
                throw new Exception("Image file " + fileName + " not loaded " + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStoref(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height,
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    private int createVAD() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = BufferAllocator.allocateBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = BufferAllocator.allocateBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        if (attributeNumber == 0) {
            GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
        } else if (attributeNumber == 1) {
            GL20.glVertexAttribPointer(attributeNumber, 2, GL11.GL_FLOAT, false, 2 * Float.BYTES, 0);
        }
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL30.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }


}
