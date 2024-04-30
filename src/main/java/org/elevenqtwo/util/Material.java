package org.elevenqtwo.util;

public class Material {
    private int textureId;
    private String textureFile;

    public Material() {}

    public void setTextureFile(String textureFile) {
        this.textureFile = textureFile;
    }

    public String getTextureFile() {
        return textureFile;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return textureId;
    }
}
