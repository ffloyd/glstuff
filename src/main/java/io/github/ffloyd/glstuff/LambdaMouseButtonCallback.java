package io.github.ffloyd.glstuff;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;
import java.util.function.BiConsumer;

import static org.lwjgl.glfw.GLFW.*;

public class LambdaMouseButtonCallback extends GLFWMouseButtonCallback {
    private BiConsumer<java.lang.Double, java.lang.Double> onLeftPress;
    private BiConsumer<java.lang.Double, java.lang.Double> onRightPress;

    public LambdaMouseButtonCallback(
            BiConsumer<java.lang.Double, java.lang.Double> onLeftPress,
            BiConsumer<java.lang.Double, java.lang.Double> onRightPress) {
        this.onLeftPress = onLeftPress;
        this.onRightPress = onRightPress;
    }

    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(window, xBuffer, yBuffer);

            double x = xBuffer.get();
            double y = yBuffer.get();

            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                onLeftPress.accept(x, y);
            }

            if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                onRightPress.accept(x, y);
            }
        }
    }
}
