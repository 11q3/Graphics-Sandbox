package org.elevenqtwo.graphics;

import org.joml.Vector3f;

public class Entity {

    private final Model model;
    private final Vector3f pos;
    private final Vector3f rotation;
    private final float scale;

    public Entity(Model model, Vector3f pos, Vector3f rotation, float scale) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void incrementRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }
}
