package org.game.application;

import org.game.engine.Camera2D;
import org.game.engine.RenderCommand;
import org.game.engine.Renderer2D;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class FlappyBirdLogic {

    private Camera2D sceneCamera;
    private Vector3f cameraPosition;

    public void start() {
        cameraPosition = new Vector3f(1.0f, 0.0f, 0.0f);
        sceneCamera = new Camera2D(cameraPosition, 1600, 900); // TEMP: Change it!

        Renderer2D.init();
    }

    public void update(double timestep) {
        RenderCommand.ClearScreen(0.1f, 0.1f, 0.1f, 1.0f);

        Renderer2D.beginScene(sceneCamera);
        updateGame(timestep);
        Renderer2D.endScene();
    }

    private void updateGame(double timestep) {
        Renderer2D.drawClearQuad(
                new Vector3f(0.0f, 0.0f, 0.0f),
                20.0f,
                new Vector2f(1.0f, 0.5f),
                new Vector4f(1.0f, 0.0f, 0.0f,1.0f)
        );
    }
}
