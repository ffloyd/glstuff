package io.github.ffloyd.glstuff.shaders;

import io.github.ffloyd.glstuff.BuildableOnce;
import io.github.ffloyd.glstuff.UBO.SimpleUBO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;

import java.util.Arrays;
import java.util.function.Consumer;

public class ShaderProgram extends BuildableOnce {
    private Shader[] shaders;

    public ShaderProgram(Shader[] shaders) {
        this.shaders = shaders;
    }

    @Override
    protected int processBuild() {
        int program = GL20.glCreateProgram();

        Integer[] compiledShaders = Arrays.stream(shaders).map(Shader::getReference).toArray(Integer[]::new);
        Arrays.stream(compiledShaders).forEach(shaderId -> GL20.glAttachShader(program, shaderId));

        GL20.glLinkProgram(program);

        int status = GL20.glGetShaderi(program, GL20.GL_LINK_STATUS);
        if (status == GL11.GL_FALSE){
            String error = GL20.glGetProgramInfoLog(program);
            throw new RuntimeException("Linker failure: "+ error);
        }

        Arrays.stream(compiledShaders).forEach(shaderId -> GL20.glDetachShader(program, shaderId));

        return program;
    }

    public void setUniformVariable(String name, Consumer<Integer> setter) {
        GL20.glUseProgram(getReference());
        int location = GL20.glGetUniformLocation(getReference(), name);
        setter.accept(location);
        GL20.glUseProgram(0);
    }

    public void bindUBO(SimpleUBO ubo, String parameter) {
        GL20.glUseProgram(getReference());
        int index = GL31.glGetUniformBlockIndex(getReference(), parameter);
        if (index == GL31.GL_INVALID_INDEX) {
            throw new RuntimeException("Cannot find uniform block in program: " + parameter);
        }
        GL31.glUniformBlockBinding(getReference(), index, ubo.getBindingPoint());
        GL20.glUseProgram(0);
    }
}
