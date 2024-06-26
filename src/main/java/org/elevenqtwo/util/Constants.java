package org.elevenqtwo.util;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Constants {
    public static final String TITLE = "graphics";
    public static final float CAMERA_SPEED = 1f;
    public static final float CAMERA_SENSITIVITY = 0.25f;
    public static final long NANOSECOND = 1000000000L;
    public static final float TARGET_FRAMERATE = 6000;
    public static final float FRAMETIME = 1.0f / TARGET_FRAMERATE;
    public static  final Vector3f AMBIENT_LIGHT = new Vector3f(0.3f,0.3f, 0.3f);
    public static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

}
