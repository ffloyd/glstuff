package io.github.ffloyd.glstuff;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

abstract public class SimpleGLFWApplication {
    private String  windowTitle;
    private int     windowWidth;
    private int     windowHeight;

    private long window;

    private GLFWErrorCallback   errorCallback;
    private GLFWKeyCallback     keyCallback;

    public SimpleGLFWApplication(String windowTitle, int windowWidth, int windowHeight) {
        this.windowTitle    = windowTitle;
        this.windowHeight   = windowHeight;
        this.windowWidth    = windowWidth;
    }

    public void run() {
        try {
            if (glfwInit() != GLFW_TRUE) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }

            System.out.println("LWJGL version: " + Version.getVersion());
            System.out.println("GLFW version: " + glfwGetVersionString());

            glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint());

            setWindowHints();

            window = glfwCreateWindow(windowWidth, windowHeight, windowTitle, NULL, NULL);
            if (window == NULL) {
                throw new RuntimeException("Cannot create window");
            }

            glfwMakeContextCurrent(window);

            if (keyCallback != null) {
                glfwSetKeyCallback(window, keyCallback);
            }

            glfwSwapInterval(1); // enable vsync
            GL.createCapabilities();
            System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

            glfwShowWindow(window);

            beforeLoop();
            loop();

            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            errorCallback.release();
            if (keyCallback != null) {
                keyCallback.release();
            }
        }
    }

    protected void setWindowHints() {
        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_RESIZABLE,  GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE,    GLFW_FALSE);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,  3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,  3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT,  GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE,         GLFW_OPENGL_CORE_PROFILE);
    }

    public void setKeyCallback(GLFWKeyCallback callback) {
        keyCallback = callback;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    private void loop() {
        while (glfwWindowShouldClose(window) == GLFW_FALSE) {
            eachFrame();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    abstract protected void beforeLoop(); // place for OpenGL initialization
    abstract protected void eachFrame();  // place for main logic
}
