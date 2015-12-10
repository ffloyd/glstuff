import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class SimpleFloatVBO implements SimpleVBO {
    private FloatBuffer javaBuffer;
    private int buffer;
    private int dims;

    public SimpleFloatVBO(float[] data, int dims) {
        javaBuffer = BufferUtils.createFloatBuffer(data.length);
        javaBuffer.put(data);
        javaBuffer.flip();

        this.dims = dims;
    }

    public SimpleFloatVBO(float[] data) {
        this(data, 3);
    }

    public void upload(int usage) {
        buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, javaBuffer, usage);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind
    }

    public void upload() {
        upload(GL15.GL_STATIC_DRAW);
    }

    public void bind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL20.glVertexAttribPointer(0, dims, GL11.GL_FLOAT, false, 0, 0);
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public int get() {
        return buffer;
    }
}
