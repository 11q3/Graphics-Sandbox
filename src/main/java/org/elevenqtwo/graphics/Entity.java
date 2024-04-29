package org.elevenqtwo.graphics;

import org.joml.Vector3f;

public class Entity {

    private final Model model;
    private Vector3f position;
    private Vector3f rotation;
    private final float scale;

    public Entity(Model model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void incrementRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void movePosition(float x, float y, float z) {
        if (z!= 0) {
            position.x += (float) (Math.sin(Math.toRadians(rotation.y)) * -1.0 * z);
            position.z += (float) (Math.cos(Math.toRadians(rotation.y)) * z);
        }

        if (x!= 0) {
            position.x += (float) (Math.sin(Math.toRadians(rotation.y - 90)) * -1.0 * x);
            position.z += (float) (Math.cos(Math.toRadians(rotation.y - 90)) * x);
        }

        position.y += y;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
