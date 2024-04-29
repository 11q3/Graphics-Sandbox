package org.elevenqtwo.game;

import org.joml.Vector3f;

public class Camera {

    private final Vector3f position;
    private final Vector3f rotation;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
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

    public void moveRotation(float x, float y, float z ) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
