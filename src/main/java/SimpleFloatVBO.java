import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class SimpleFloatVBO {
    private FloatBuffer javaBuffer;
    private int bufferId;
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

    public void uploadBuffer(int usage) {
        bufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, javaBuffer, usage);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind
    }

    public void uploadBuffer() {
        uploadBuffer(GL15.GL_STATIC_DRAW);
    }

    public void enableBuffer() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferId);
        GL20.glVertexAttribPointer(0, dims, GL11.GL_FLOAT, false, 0, 0);
    }
}
