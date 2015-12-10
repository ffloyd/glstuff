import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Shader {
    private String fileName;
    private int shaderType;

    public int compiledShader;
    boolean compiled;

    public Shader(String fileName, int shaderType) {
        this.fileName = fileName;
        this.shaderType = shaderType;
    }

    public int compile() {
        if (!compiled) {
            String code = getCode();
            compiledShader = GL20.glCreateShader(shaderType);
            GL20.glShaderSource(compiledShader, code);
            GL20.glCompileShader(compiledShader);

            int status = GL20.glGetShaderi(compiledShader, GL20.GL_COMPILE_STATUS);
            if (status == GL11.GL_FALSE) {

                String error = GL20.glGetShaderInfoLog(compiledShader);

                String ShaderTypeString = null;
                switch(shaderType){
                    case GL20.GL_VERTEX_SHADER:     ShaderTypeString = "vertex";    break;
                    case GL32.GL_GEOMETRY_SHADER:   ShaderTypeString = "geometry";  break;
                    case GL20.GL_FRAGMENT_SHADER:   ShaderTypeString = "fragment";  break;
                }

                throw new RuntimeException("Compile failure in " + ShaderTypeString + " shader:\n" + error);
            }
        }
        return compiledShader;
    }

    private String getCode() {
        InputStream input = this.getClass().getResourceAsStream(fileName);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
        return buffer.lines().collect(Collectors.joining("\n"));
    }
}
