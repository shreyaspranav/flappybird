package org.game.engine;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL46.*;

public class ShaderProgram {

    private String vertexShaderCode, fragmentShaderCode;
    private final int shaderProgramID;
    public ShaderProgram(String vertexShaderFilePath, String fragmentShaderFilePath) {

        // Read the two files:
        try {
            // Read Vertex Shader:
            InputStream inputStream = ShaderProgram.class.getClassLoader().getResourceAsStream(vertexShaderFilePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str + "\r\n");
            }

            vertexShaderCode = sb.toString();

            inputStream = ShaderProgram.class.getClassLoader().getResourceAsStream(fragmentShaderFilePath);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            str = "";
            sb = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str + "\r\n");
            }

            fragmentShaderCode = sb.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        shaderProgramID = glCreateProgram();
    }

    public void loadShader() {
        int[] shaderTypes = { GL_VERTEX_SHADER, GL_FRAGMENT_SHADER };

        Map<Integer, String> shaderSources = new HashMap<>(2);
        shaderSources.put(GL_VERTEX_SHADER, vertexShaderCode);
        shaderSources.put(GL_FRAGMENT_SHADER, fragmentShaderCode);

        Map<Integer, String> shaderNames = new HashMap<>(2);
        shaderNames.put(GL_VERTEX_SHADER, "GL_VERTEX_SHADER");
        shaderNames.put(GL_FRAGMENT_SHADER, "GL_FRAGMENT_SHADER");

        for(int shaderType : shaderTypes) {
            int shader = glCreateShader(shaderType);
            glShaderSource(shader, shaderSources.get(shaderType));

            glCompileShader(shader);

            try (MemoryStack stack = MemoryStack.stackPush()){
                IntBuffer compileStatus = stack.mallocInt(1); // Basically using malloc to create int*
                glGetShaderiv(shader, GL_COMPILE_STATUS, compileStatus);

                if(compileStatus.get(0) == GL_FALSE) {
                    System.err.println(shaderNames.get(Integer.valueOf(shaderType)) + " Failed to Compile!");
                    System.err.println(glGetShaderInfoLog(shader));
                }
            }

            glAttachShader(shaderProgramID, shader);
        }

        glLinkProgram(shaderProgramID);

        try (MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer linkStatus = stack.mallocInt(1);
            glGetProgramiv(shaderProgramID, GL_LINK_STATUS, linkStatus);

            if(linkStatus.get(0) == GL_FALSE) {
                System.err.println("Shader Program Failed to Link!");
                System.err.println(glGetProgramInfoLog(shaderProgramID));
            }
        }
    }

    public void bind() {
        glUseProgram(shaderProgramID);
    }
    public void unBind() {
        glUseProgram(0);
    }

    public void uniformMat4(String uniformName, Matrix4f mat) {
        int loc = glGetUniformLocation(shaderProgramID, uniformName);

        if(loc == -1)
            throw new RuntimeException("Uniform variable: '" + uniformName + "' not found");

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer matrix = mat.get(stack.mallocFloat(16));
            glUniformMatrix4fv(loc, false, matrix);
        }
    }

    public void uniformIntArray(String uniformName, int[] arr) {
        int loc = glGetUniformLocation(shaderProgramID, uniformName);

        if(loc == -1)
            throw new RuntimeException("Uniform variable: '" + uniformName + "' not found");
        glUniform1iv(loc, arr);
    }
}
