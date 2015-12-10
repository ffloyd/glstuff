import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class PointsVBO implements SimpleVBO {
    private FloatBuffer javaBuffer;
    private int buffer;
    private int dims;
    private int elementsCount;

    public PointsVBO(float[] data, int dims) {
        javaBuffer = BufferUtils.createFloatBuffer(data.length);
        javaBuffer.put(data);
        javaBuffer.flip();

        this.dims = dims;

        this.elementsCount = data.length / dims;
    }

    public PointsVBO(float[] data) {
        this(data, 3);
    }

    @Override
    public void upload(int usage) {
        buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, javaBuffer, usage);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind
    }

    @Override
    public void upload() {
        upload(GL15.GL_STATIC_DRAW);
    }

    @Override
    public void bind(int attributeIndex) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL20.glVertexAttribPointer(attributeIndex, dims, GL11.GL_FLOAT, false, 0, 0);
    }

    @Override
    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public int get() {
        return buffer;
    }

    @Override
    public int getElementsCount() {
        return elementsCount;
    }
}
