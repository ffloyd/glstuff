package io.github.ffloyd.glstuff.UBO;

import io.github.ffloyd.glstuff.BuildableOnce;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public abstract class SimpleUBO<InputType> extends BuildableOnce {
    private int bindingPoint;
    private boolean bound;

    public int getBindingPoint() {
        if (bound) {
            return bindingPoint;
        } else {
            throw new RuntimeException("Buffer not bound to any binding point");
        }
    }

    public void setBindingPoint(int bindingPoint) {
        if (!bound) {
            this.bindingPoint = bindingPoint;
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, getReference());
            GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, bindingPoint, getReference());
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
            this.bound = true;
        } else {
            throw new RuntimeException("Buffer already bound to binding point");
        }
    }

    abstract public void update(InputType data);
}
