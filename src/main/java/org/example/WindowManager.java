package org.example;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000;

    private final String title;

    private int width;
    private int heigth;

    private long window;

    private boolean resize;

    private boolean vSync;
    private final Matrix4f projectionMatrix;
    public WindowManager(String title, int width, int heigth, boolean vSync) {
        this.title = title;
        this.width = width;
        this.heigth = heigth;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

            boolean maximised = false;
            if(width == 0 || heigth == 0) {
                width = 100;
                heigth = 100;
                GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
                maximised = false;
            }

            window = GLFW.glfwCreateWindow(width, heigth, title, MemoryUtil.NULL, MemoryUtil.NULL);
            if(window == MemoryUtil.NULL) {
                throw new RuntimeException("Failed to create GLFW window");
            }

            GLFW.glfwSetFramebufferSizeCallback(window, (window, width, heigth) -> {
                this.width = width;
                this.heigth = heigth;
                this.setResize(true);
            } );

            GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
                if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                    GLFW.glfwSetWindowShouldClose(window, true);
                }
            });

            if(maximised) {
                GLFW.glfwMaximizeWindow(window);
            } else {
                GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
                GLFW.glfwSetWindowPos(window, (vidMode.width() - width) /2,
                        (vidMode.height()- heigth) /2 );
            }

            GLFW.glfwMakeContextCurrent(window);

            if(isVSync()) {
                GLFW.glfwSwapInterval(1);
            }

            GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void cleanUp() {
        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r,g,b,a);
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public int getWidth() {
        return width;
    }

    public int getHeigth() {
        return heigth;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    private boolean isVSync() {
        return vSync;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / heigth;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix4f, int width, int heigth) {
        float aspectRation = (float) width / heigth;
        return matrix4f.setPerspective(FOV, aspectRation, Z_NEAR, Z_FAR);
    }
}