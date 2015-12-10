import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;

import java.util.Arrays;

public class ShaderProgram {
    private Shader[] shaders;

    private Integer[] compiledShaders;
    private int compiledProgram;
    boolean compiled;

    public ShaderProgram(Shader[] shaders) {
        this.shaders = shaders;
    }

    public int link() {
        if (!compiled) {
            compiledProgram = GL20.glCreateProgram();

            compiledShaders = Arrays.stream(shaders).map(Shader::compile).toArray(Integer[]::new);
            Arrays.stream(compiledShaders).forEach(shaderId -> GL20.glAttachShader(compiledProgram, shaderId));

            GL20.glLinkProgram(compiledProgram);

            int status = GL20.glGetShaderi(compiledProgram, GL20.GL_LINK_STATUS);
            if (status == GL11.GL_FALSE){
                String error = GL20.glGetProgramInfoLog(compiledProgram);
                throw new RuntimeException("Linker failure: "+ error);
            }

            Arrays.stream(compiledShaders).forEach(shaderId -> GL20.glDetachShader(compiledProgram, shaderId));
        }
        return compiledProgram;
    }

    public void bindUBO(SimpleUBO ubo, String parameter) {
        GL20.glUseProgram(compiledProgram);
        int index = GL31.glGetUniformBlockIndex(compiledProgram, parameter);
        if (index == GL31.GL_INVALID_INDEX) {
            throw new RuntimeException("Cannot find uniform block in program: " + parameter);
        }
        GL31.glUniformBlockBinding(compiledProgram, index, ubo.getBindingPoint());
        GL20.glUseProgram(0);
    }

    public int get() {
        return compiledProgram;
    }
}
