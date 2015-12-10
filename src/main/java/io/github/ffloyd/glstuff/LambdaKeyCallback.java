package io.github.ffloyd.glstuff;

import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class LambdaKeyCallback extends BasicKeyCallback {
    Consumer<Integer> lambda;

    public LambdaKeyCallback(Consumer<Integer> lambda) {
        this.lambda = lambda;
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_RELEASE) {
            lambda.accept(key);
        }
        super.invoke(window, key, scancode, action, mods);
    }
}
