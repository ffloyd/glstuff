import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.Arrays;
import java.util.stream.Stream;

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
}
