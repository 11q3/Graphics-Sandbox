package org.elevenqtwo.graphics;

import org.elevenqtwo.util.BufferAllocator;
import org.elevenqtwo.util.ResourceLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
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

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s");
            switch (tokens[0]) {
                case "v":
                    Vector3f verticesVector = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(verticesVector);
                    break;
                case "vt":
                    Vector2f texturesVector = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(texturesVector);
                    break;
                case "vn":
                    Vector3f normalsVector = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(normalsVector);
                    break;
                case "f":
                    List<Vector3i> facesTemp = new ArrayList<>();
                    processFace(tokens[1], faces, facesTemp);
                    processFace(tokens[2], faces, facesTemp);
                    processFace(tokens[3], faces, facesTemp);

                    for (int i = 1; i < facesTemp.size() - 1; i++) {
                        faces.add(facesTemp.get(0));
                        faces.add(facesTemp.get(i));
                        faces.add(facesTemp.get(i + 1));
                    }
                    break;
                default:
                    break;
            }
        }

        List<Integer> indices = new ArrayList<>();
        float[] verticesArray = new float[vertices.size() * 3];
        int i = 0;
        for (Vector3f pos : vertices) {
            verticesArray[i * 3] = pos.x;
            verticesArray[i * 3 + 1] = pos.y;
            verticesArray[i * 3 + 2] = pos.z;
            i++;
        }

        float[] textureCoordinatesArray = new float[vertices.size() * 2];
        float[] normalArray = new float[vertices.size() * 3];

        for (Vector3i face : faces) {
            processVertex(face.x, face.y, face.z, textures, normals, indices, textureCoordinatesArray, normalArray);
        }

        int[] indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();

        return loadModel(verticesArray, textureCoordinatesArray, normalArray, indicesArray);
    }


    public Model loadModel(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
        int id = createVAD();
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        //storeDataInAttributeList(2, 3, textureCoords);
        //storeDataInAttributeList(3, 4, normals);

        unbind();
        return new Model(id, indices.length);
    }

    private static void processFace(String token, List<Vector3i> faces, List<Vector3i> facesTemp) {
        String[] lineToken = token.split("/");
        int length = lineToken.length;
        int position = -1;
        int coordinates = -1;
        int normal = -1;

        position = Integer.parseInt(lineToken[0]) - 1;
        if (length > 1) {
            String textureCoordinates = lineToken[1];
            coordinates =!textureCoordinates.isEmpty()? Integer.parseInt(textureCoordinates) - 1 : -1;

            if (length > 2) {
                normal = Integer.parseInt(lineToken[2]) - 1;
            }
        }

        facesTemp.add(new Vector3i(position, coordinates, normal));
    }


    public static void processVertex(int position, int textureCoordinate, int normal,
                                     List<Vector2f> textureCoordinatesList,
                                     List<Vector3f> normalList, List<Integer> indicesList,
                                     float[] textureCoordinateArray, float[] normalArray) {
        indicesList.add(position);

        if (textureCoordinate >= 0) {
            Vector2f textureCoordinateVector = textureCoordinatesList.get(textureCoordinate);
            textureCoordinateArray[position * 2] = textureCoordinateVector.x;
            textureCoordinateArray[position * 2 + 1] = 1- textureCoordinateVector.y;
        }

        if (normal >= 0) {
            Vector3f normalVector = normalList.get(normal);
            normalArray[position * 3] = normalVector.x;
            normalArray[position * 3 + 1] = normalVector.y;
            normalArray[position * 3 + 2] = normalVector.z;
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

    private void storeDataInAttributeList(int attributeNumber,int size, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = BufferAllocator.allocateBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        if (attributeNumber == 0) {
            GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
        } else if (attributeNumber == 1) {
            GL20.glVertexAttribPointer(attributeNumber, 2, GL11.GL_FLOAT, false, 2 * Float.BYTES, 0);
        } else if (attributeNumber == 2) {
            GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        }
        GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(attributeNumber);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
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