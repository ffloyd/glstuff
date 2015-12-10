package io.github.ffloyd.glstuff.shaders;

import io.github.ffloyd.glstuff.BuildableOnce;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import java.io.IOException;

public class Shader extends BuildableOnce {
    private String fileName;
    private int shaderType;

    public Shader(String fileName, int shaderType) {
        this.fileName   = fileName;
        this.shaderType = shaderType;
    }

    @Override
    protected int processBuild() {
        String shaderSource = getCode();
        int shader = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shader, shaderSource);
        GL20.glCompileShader(shader);

        int status = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (status == GL11.GL_FALSE) {

            String error = GL20.glGetShaderInfoLog(shader);

            String ShaderTypeString = null;
            switch(shaderType){
                case GL20.GL_VERTEX_SHADER:     ShaderTypeString = "vertex";    break;
                case GL32.GL_GEOMETRY_SHADER:   ShaderTypeString = "geometry";  break;
                case GL20.GL_FRAGMENT_SHADER:   ShaderTypeString = "fragment";  break;
            }

            throw new RuntimeException("Compile failure in " + ShaderTypeString + " shader:\n" + error);
        }

        return shader;
    }

    private String getCode() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            return IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Error reading shader file " + fileName + ":\n" + e.getMessage());
        }
    }
}
