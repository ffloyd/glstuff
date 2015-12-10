import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import java.nio.FloatBuffer;

public class FloatArrayUBO implements SimpleUBO<float[]> {
    private FloatBuffer floats;
    private int buffer;
    private int bindingPoint;

    FloatArrayUBO(float[] data) {
        floats = convertToBuffer(data);
    }

    @Override
    public void build(int bindingPoint) {
        buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, buffer);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, floats, GL15.GL_DYNAMIC_DRAW);

        this.bindingPoint = bindingPoint;
        GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, bindingPoint, buffer);

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
    }

    @Override
    public void update(float[] data) {
        floats = convertToBuffer(data);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, buffer);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, floats);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
    }

    private FloatBuffer convertToBuffer(float[] data) {
        FloatBuffer result = BufferUtils.createFloatBuffer(data.length);
        result.put(data);
        result.flip();
        return result;
    }

    @Override
    public int getBindingPoint() {
        return bindingPoint;
    }
}
