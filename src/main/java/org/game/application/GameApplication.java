package org.game.application;

import org.game.engine.*;

import java.util.ArrayList;

public class GameApplication implements Application {

    private ApplicationProperties properties;
    private Window window;

    public GameApplication(ApplicationProperties properties) { this.properties = properties; }

    VertexBuffer buffer;
    ShaderProgram shader;
    @Override
    public void onStart() {
        //System.out.println("Start!");
        window = new Window(properties);
        window.createWindow();

        float[] quadVertices = {
                // aPosition:             // aColor:                      // aTextureCoords:  // aTexSlot:
                 0.5f,  0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f,         1.0f, 1.0f,         0.0f,
                -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f,         0.0f, 1.0f,         0.0f,
                -0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 1.0f, 1.0f,         0.0f, 0.0f,         0.0f,
                 0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 1.0f, 1.0f,         1.0f, 0.0f,         0.0f,

        };

        int[] indices = { 0, 1, 2, 0, 2, 3 };

        ArrayList<BufferElement> bufferLayout = new ArrayList<>();
        bufferLayout.add(new BufferElement("aPosition", 3, 0));
        bufferLayout.add(new BufferElement("aColor", 4, 1));
        bufferLayout.add(new BufferElement("aTextureCoords", 2, 2));
        bufferLayout.add(new BufferElement("aTextureSlot", 1, 3));

        buffer = new VertexBuffer(bufferLayout, 10 * 4 * 4);
        buffer.put(quadVertices);

        ElementBuffer elementBuffer = new ElementBuffer(6 * 4);
        elementBuffer.put(indices);

        buffer.setElementBuffer(elementBuffer);

        shader = new ShaderProgram("shaders/vertexShader.glsl", "shaders/fragmentShader.glsl");
        shader.loadShader();
    }

    @Override
    public void onUpdate(double timestep) {
        RenderCommand.ClearScreen(0.1f, 0.1f, 0.1f, 1.0f);

        shader.bind();
        RenderCommand.DrawIndexed(buffer, 0, 6);

        window.updateWindow();
    }

    @Override
    public void onExit() {
        System.out.println("Exit");
        window.destroyWindow();
    }
    @Override
    public boolean isApplicationRunning() {
        return !window.isWindowClosing();
    }
}
