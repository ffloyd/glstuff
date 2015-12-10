package io.github.ffloyd.glstuff.UBO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.nio.FloatBuffer;

public class FloatArrayUBO extends SimpleUBO<float[]> {
    private FloatBuffer floats;

    FloatArrayUBO(float[] data) {
        floats = convertToBuffer(data);
    }

    @Override
    protected int processBuild() {
        int buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, buffer);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, floats, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        return buffer;
    }

    @Override
    public void update(float[] data) {
        floats = convertToBuffer(data);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, getReference());
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, floats);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
    }

    private FloatBuffer convertToBuffer(float[] data) {
        FloatBuffer result = BufferUtils.createFloatBuffer(data.length);
        result.put(data);
        result.flip();
        return result;
    }
}
