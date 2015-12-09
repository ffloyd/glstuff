import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

abstract public class GLFWApplication {
    private String  windowTitle;
    private int     windowWidth;
    private int     windowHeight;

    private long window;

    private GLFWErrorCallback   errorCallback;
    private GLFWKeyCallback     keyCallback;

    public GLFWApplication(String windowTitle, int windowWidth, int windowHeight) {
        this.windowTitle    = windowTitle;
        this.windowHeight   = windowHeight;
        this.windowWidth    = windowWidth;
    }

    public void run() {
        try {
            if (glfwInit() != GLFW_TRUE) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }

            printSystemInfo();

            glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint());

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

    public void setKeyCallback(GLFWKeyCallback callback) {
        keyCallback = callback;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    private void printSystemInfo() {
        System.out.println("LWJGL version: " + Version.getVersion());
        System.out.println("GLFW version: " + glfwGetVersionString());
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
