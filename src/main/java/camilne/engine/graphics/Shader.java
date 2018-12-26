package camilne.engine.graphics;

import camilne.engine.GLUtil;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private Map<String, Integer> uniforms;
    private int program;

    public Shader(String vertexPath, String fragmentPath) throws IOException {
        uniforms = new HashMap<>();

        program = glCreateProgram();
        GLUtil.checkError();
        var vertexShaderId = loadShader(vertexPath, GL_VERTEX_SHADER, program);
        glAttachShader(program, vertexShaderId);
        GLUtil.checkError();
        var fragmentShaderId = loadShader(fragmentPath, GL_FRAGMENT_SHADER, program);
        glAttachShader(program, fragmentShaderId);
        GLUtil.checkError();

        glLinkProgram(program);
        GLUtil.checkError();
        glValidateProgram(program);
        GLUtil.checkError();

        glDetachShader(program, vertexShaderId);
        glDetachShader(program, fragmentShaderId);
        GLUtil.checkError();

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        GLUtil.checkError();
    }

    private static int loadShader(String path, int type, int program) throws IOException {
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
        GLUtil.checkError();
        glShaderSource(id, source);
        GLUtil.checkError();
        glCompileShader(id);
        GLUtil.checkError();

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error compiling shader: " + glGetShaderInfoLog(id));
        }

        if (type == GL_VERTEX_SHADER) {
            var index = 0;
            for (var line : source.toString().split("\n")) {
                var tokens = line.split("\\s+");
                if (tokens.length >= 3) {
                    for (var i = 0; i < tokens.length - 2; i++) {
                        if (tokens[i].equals("in")) {
                            glBindAttribLocation(program, index++, tokens[i + 2].trim());
                            GLUtil.checkError();
                        }
                    }
                }
            }
        }

        return id;
    }

    public void bind() {
        glUseProgram(program);
    }

    public void addUniform(String name) {
        var location = glGetUniformLocation(program, name);
        GLUtil.checkError();

        if (location == -1) {
            throw new RuntimeException("Uniform does not exist in shader: " + name);
        }

        uniforms.put(name, location);
    }

    public void setUniform(String name, Matrix4f mat) {
        var fb = BufferUtils.createFloatBuffer(16);
        mat.get(fb);
        glUniformMatrix4fv(uniforms.get(name), false, fb);
        GLUtil.checkError();
    }

}
