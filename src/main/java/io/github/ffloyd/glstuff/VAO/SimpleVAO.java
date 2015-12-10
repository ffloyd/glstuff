package io.github.ffloyd.glstuff.VAO;

import io.github.ffloyd.glstuff.BuildableOnce;
import io.github.ffloyd.glstuff.VBO.PointsVBO;
import io.github.ffloyd.glstuff.shaders.ShaderProgram;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SimpleVAO extends BuildableOnce {
    private PointsVBO vbo;
    private ShaderProgram shaderProgram;

    public SimpleVAO(PointsVBO vbo, ShaderProgram shaderProgram) {
        this.vbo = vbo;
        this.shaderProgram = shaderProgram;
    }

    public void draw() {
        GL30.glBindVertexArray(getReference());
        GL20.glUseProgram(shaderProgram.getReference());

        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vbo.getElementsCount());
        GL20.glDisableVertexAttribArray(0);

        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
    }

    @Override
    protected int processBuild() {
        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo.bind(0);
        GL20.glBindAttribLocation(shaderProgram.getReference(), 0, "position");
        vbo.unbind();

        GL30.glBindVertexArray(0);
        return vao;
    }
}
