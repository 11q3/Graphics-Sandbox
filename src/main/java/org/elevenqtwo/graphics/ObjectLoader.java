package org.elevenqtwo.graphics;

import org.elevenqtwo.util.BufferAllocator;
import org.elevenqtwo.util.Material;
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

    public Model loadModel(String objFile, String mtlFile) throws Exception {
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoordinates = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<String> materials = new ArrayList<>();

        loadObjFile(objFile, vertices, textureCoordinates, normals, indices, materials);
        loadMtlFile(mtlFile, materials);

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
        for (String material : materials) {
            Material m = loadMaterial(mtlFile, material);
            int textureId = loadTexture(m.getTextureFile());
            Texture texture = new Texture(textureId);
            model.setTexture(texture);
        }

        return model;
    }

    private Material loadMaterial(String mtlFile, String materialName) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(mtlFile))) {
            String line;
            Material material = new Material();
            while ((line = reader.readLine())!= null) {
                if (line.startsWith("newmtl " + materialName)) {
                    material = new Material();
                } else if (line.startsWith("map_Kd ")) {
                    String textureFile = line.split(" ")[1];
                    material.setTextureFile("src/main/resources/models/floppacube/floppacube/" + textureFile);
                }
            }
            return material;
        }
    }

    private int loadTexture(String material) throws Exception {
        int width;
        int height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(material, w, h, c, 4);
            if (buffer == null) {
                throw new Exception("Image file " + material + " not loaded " + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height,
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStoref(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    // Update the loadObjFile method to load normals
    private void loadObjFile(String objFile, List<Float> vertices, List<Float> textureCoords, List<Float> normals, List<Integer> indices, List<String> materials) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(objFile))) {
            String line;
            while ((line = reader.readLine())!= null) {
                if (line.startsWith("v ")) {
                    String[] vertex = line.split(" ");
                    vertices.add(Float.parseFloat(vertex[1]));
                    vertices.add(Float.parseFloat(vertex[2]));
                    vertices.add(Float.parseFloat(vertex[3]));
                } else if (line.startsWith("vt ")) {
                    String[] textureCoord = line.split(" ");
                    textureCoords.add(Float.parseFloat(textureCoord[1]));
                    textureCoords.add(Float.parseFloat(textureCoord[2]));
                } else if (line.startsWith("vn ")) {
                    String[] normal = line.split(" ");
                    normals.add(Float.parseFloat(normal[1]));
                    normals.add(Float.parseFloat(normal[2]));
                    normals.add(Float.parseFloat(normal[3]));
                } else if (line.startsWith("f ")) {
                    String[] face = line.split(" ");
                    for (int i = 1; i < face.length; i++) {
                        String[] vertexIndex = face[i].split("/");
                        indices.add(Integer.parseInt(vertexIndex[0]) - 1);
                        if (vertexIndex.length > 1 &&!vertexIndex[1].isEmpty()) {
                            int textureIndex = Integer.parseInt(vertexIndex[1]) - 1;
                            textureCoords.add(textureCoords.get(textureIndex * 2));
                            textureCoords.add(textureCoords.get(textureIndex * 2 + 1));
                        }
                        if (vertexIndex.length > 2 &&!vertexIndex[2].isEmpty()) {
                            int normalIndex = Integer.parseInt(vertexIndex[2]) - 1;
                            normals.add(normals.get(normalIndex * 3));
                            normals.add(normals.get(normalIndex * 3 + 1));
                            normals.add(normals.get(normalIndex * 3 + 2));
                        }
                    }
                } else if (line.startsWith("usemtl ")) {
                    materials.add(line.split(" ")[1]);
                }
            }
        }
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

    private void loadObjFile(String objFile, List<Float> vertices, List<Float> textureCoords,
                             List<Integer> indices, List<String> materialsUsed) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(objFile))) {
            String line;
            while ((line = reader.readLine())!= null) {
                if (line.startsWith("v ")) {
                    String[] vertex = line.split(" ");
                    vertices.add(Float.parseFloat(vertex[1]));
                    vertices.add(Float.parseFloat(vertex[2]));
                    vertices.add(Float.parseFloat(vertex[3]));
                } else if (line.startsWith("vt ")) {
                    String[] textureCoord = line.split(" ");
                    textureCoords.add(Float.parseFloat(textureCoord[1]));
                    textureCoords.add(Float.parseFloat(textureCoord[2]));
                } else if (line.startsWith("f ")) {
                    String[] face = line.split(" ");
                    for (int i = 1; i < face.length; i++) {
                        String[] vertexIndex = face[i].split("/");
                        indices.add(Integer.parseInt(vertexIndex[0]) - 1);
                    }
                } else if (line.startsWith("usemtl ")) {
                    materialsUsed.add(line.split(" ")[1]);
                }
            }
        }
    }

    private void loadMtlFile(String mtlFile, List<String> materials) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(mtlFile))) {
            String line;
            Material currentMaterial = null;
            while ((line = reader.readLine())!= null) {
                if (line.startsWith("newmtl ")) {
                    String materialName = line.split(" ")[1];
                    currentMaterial = new Material();
                    materials.add(materialName);
                } else if (line.startsWith("map_Kd ")) {
                    String textureFile = line.split(" ")[1];
                    assert currentMaterial != null;
                    currentMaterial.setTextureFile(textureFile);
                }
            }
        }
    }
}
