package org.elevenqtwo.graphics;

import org.elevenqtwo.util.BufferAllocator;
import org.elevenqtwo.util.ResourceLoader;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {
    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();
    private final List<Integer> textures = new ArrayList<>();

    public Model loadOBJModel(String fileName) {
        List<String> lines = ResourceLoader.readAllLines(fileName);

        List<Vector3f>  vertices = new ArrayList<>();
        List<Vector3f>  normals = new ArrayList<>();
        List<Vector3f>  textures = new ArrayList<>();
        List<Vector3f>  faces = new ArrayList<>();

        for(String line : lines)  {
            String[] tokens = line.split();
        }


    }

    public Model loadModel(String objFile, String mtlFile) throws Exception {
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoordinates = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<String> materials = new ArrayList<>();

        //loadObjFile(objFile, vertices, textureCoordinates, normals, indices, materials);
        //loadMtlFile(mtlFile, materials);

        float[] verticesArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            verticesArray[i] = vertices.get(i);
        }

        float[] textureCoordsArray = new float[textureCoordinates.size()];
        for (int i = 0; i < textureCoordinates.size(); i++) {
            textureCoordsArray[i] = textureCoordinates.get(i);
        }

        float[] normalsArray = new float[normals.size()]; // Create a float array for normals
        for (int i = 0; i < normals.size(); i++) {
            normalsArray[i] = normals.get(i);
        }

        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        int vaoId = createVAD();
        storeIndicesBuffer(indicesArray);
        storeDataInAttributeList(0, verticesArray, 3); // position
        storeDataInAttributeList(1, normalsArray, 3); // normals
        storeDataInAttributeList(2, textureCoordsArray, 2); // texture coordinates
        unbind();

        Model model = new Model(vaoId, indicesArray.length);

        return model;
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

    private void storeDataInAttributeList(int attributeNumber, float[] data, int size) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = BufferAllocator.allocateBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(attributeNumber);
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
