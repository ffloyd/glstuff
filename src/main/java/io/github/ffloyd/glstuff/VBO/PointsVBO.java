package io.github.ffloyd.glstuff.VBO;

import io.github.ffloyd.glstuff.BuildableOnce;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class PointsVBO extends BuildableOnce {
    private FloatBuffer floatBuffer;
    private int usage;

    private int dims;
    private int elementsCount;

    public PointsVBO(float[] data, int dims, int usage) {
        floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();

        this.dims   = dims;
        this.usage  = usage;

        this.elementsCount = data.length / dims;
    }

    public int getElementsCount() {
        return elementsCount;
    }

    public void bind(int attributeIndex) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, getReference());
        GL20.glVertexAttribPointer(attributeIndex, dims, GL11.GL_FLOAT, false, 0, 0);
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    @Override
    protected int processBuild() {
        int buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, usage);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return buffer;
    }
}
