package org.elevenqtwo.game;

import org.joml.Vector3f;

public class Camera {

    private final Vector3f position;
    private final Vector3f rotation;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    public void movePosition(float x, float y, float z, double deltaTime) {
        if (z!= 0) {
            position.x += (float) (Math.sin(Math.toRadians(rotation.y)) * -1.0 * z * deltaTime);
            position.z += (float) (Math.cos(Math.toRadians(rotation.y)) * z * deltaTime);
        }

        if (x!= 0) {
            position.x += (float) (Math.sin(Math.toRadians(rotation.y - 90)) * -1.0 * x * deltaTime);
            position.z += (float) (Math.cos(Math.toRadians(rotation.y - 90)) * x * deltaTime);
        }

        position.y += (float) (y * deltaTime);
    }

    public void moveRotation(float x, float y, float z, double deltaTime) {
        this.rotation.x += (float) (x * deltaTime);
        this.rotation.y += (float) (y * deltaTime);
        this.rotation.z += (float) (z * deltaTime);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
