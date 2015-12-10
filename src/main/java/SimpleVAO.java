import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SimpleVAO {
    private int vao;
    private SimpleVBO vbo;
    private ShaderProgram shaderProgram;

    public SimpleVAO(SimpleVBO vbo, ShaderProgram shaderProgram) {
        this.vbo = vbo;
        this.shaderProgram = shaderProgram;
    }

    public void build() {
        vbo.upload();
        shaderProgram.link();

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo.bind();
        GL20.glBindAttribLocation(shaderProgram.get(), 0, "position");
        vbo.unbind();

        GL30.glBindVertexArray(0);
    }

    public void draw() {
        GL30.glBindVertexArray(vao);
        GL20.glUseProgram(shaderProgram.get());

        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vbo.getElementsCount());
        GL20.glDisableVertexAttribArray(0);

        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
    }
}
