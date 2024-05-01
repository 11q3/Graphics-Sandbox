package org.elevenqtwo.graphics;

import org.elevenqtwo.util.Constants;
import org.joml.Vector4f;

public class Material {
    private Vector4f ambientColor;
    private Vector4f diffuseColor;
    private Vector4f specularColor;
    private Texture texture;
    public float reflectance;

    public Material() {
        this.ambientColor = Constants.DEFAULT_COLOR;
        this.diffuseColor = Constants.DEFAULT_COLOR;
        this.specularColor = Constants.DEFAULT_COLOR;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Texture texture) {
        this.ambientColor = Constants.DEFAULT_COLOR;
        this.diffuseColor = Constants.DEFAULT_COLOR;
        this.specularColor = Constants.DEFAULT_COLOR;
        this.texture = texture;
        this.reflectance = 0;
    }

    public Material(Vector4f color, float reflectance) {
        this(color, color,color, reflectance, null);
    }

    public Material(Vector4f color, float reflectance, Texture texture) {
        this(color, color,color, reflectance, texture);
    }


    public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor,  float reflectance, Texture texture) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuse) {
        this.diffuseColor = diffuse;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }


    public boolean hasTexture() {
        return this.texture != null;
    }
}
