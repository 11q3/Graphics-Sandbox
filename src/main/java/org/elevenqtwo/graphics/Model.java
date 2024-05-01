package org.elevenqtwo.graphics;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final int id;
    private final int vertexCount;
    private final Material material;

    public Model(int id, int vertexCount) {
        this.id = id;
        this.vertexCount = vertexCount;
        this.material = new Material();
    }

    public Model(int id,  int vertexCount, Texture texture) {
        this.id  = id;
        this.vertexCount = vertexCount;
        this.material = new Material(texture);
    }

    public Model(Model model, Texture texture) {
        this.id = model.getId();
        this.vertexCount = model.getVertexCount();
        this.material = model.getMaterial();
        this.material.setTexture(texture);
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Material getMaterial() {
        return material;
    }

    public void setTexture(Texture texture, float reflectance) {
        this.material.setTexture(texture);
        this.material.setReflectance(reflectance);
    }
}
