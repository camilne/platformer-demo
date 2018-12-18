package camilne.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int program;

    public Shader(String vertexPath, String fragmentPath) throws IOException {
        program = glCreateProgram();
        glAttachShader(program, loadShader(vertexPath, GL_VERTEX_SHADER));
        glAttachShader(program, loadShader(fragmentPath, GL_FRAGMENT_SHADER));

        glBindAttribLocation(program, 0, "in_Position");
        glBindAttribLocation(program, 1, "in_TextureCoord");

        glLinkProgram(program);
        glValidateProgram(program);
    }

    private int loadShader(String path, int type) throws IOException {
        if (Shader.class.getClassLoader().getResource(path) == null) {
            throw new IOException("Shader does not exist: " + path);
        }

        var source = new StringBuilder();
        try (var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Shader.class.getClassLoader().getResourceAsStream(path))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }
        }

        var id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error compiling shader: " + glGetShaderInfoLog(id));
        }

        return id;
    }

    public void bind() {
        glUseProgram(program);
    }

}
