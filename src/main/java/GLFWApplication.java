import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

abstract public class GLFWApplication {
    private long window;

    public void run() {
        try {
            if (glfwInit() != GLFW_TRUE) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }

            window = glfwCreateWindow(1024, 768, "GLFW application", NULL, NULL);
            if (window == NULL) {
                throw new RuntimeException("Cannot create window");
            }

            glfwMakeContextCurrent(window);

            loop();
        } finally {
            glfwTerminate();
        }
    }

    private void loop() {
        while (glfwWindowShouldClose(window) == GLFW_FALSE) {
            each_frame();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    abstract protected void each_frame();
}
