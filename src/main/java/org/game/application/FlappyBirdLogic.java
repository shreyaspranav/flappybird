package org.game.application;

import org.game.engine.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class FlappyBirdLogic {

    private Camera2D sceneCamera;
    private Vector3f cameraPosition;
    private Vector3f playerPosition;

    float a = 0.0f;

    public void start(Window window) {
        cameraPosition = new Vector3f(1.0f, 0.0f, 0.0f);
        sceneCamera = new Camera2D(cameraPosition, window.getProperties().getWindowWidth(), window.getProperties().getWindowHeight());

        playerPosition = new Vector3f(0.0f, 0.01f, 0.0f);

        Renderer2D.init();
        Input.setWindow(window);
    }

    public void update(double timestep) {
        RenderCommand.ClearScreen(0.1f, 0.1f, 0.1f, 1.0f);

        Renderer2D.beginScene(sceneCamera);
        updateGame(timestep);
        Renderer2D.endScene();
    }

    private void updateGame(double timestep) {

        playerPosition.y -= timestep * 0.0001 * a;
        a += 0.01f;

        if(Input.isKeyPressed(KeyCode.KEY_SPACE)) {
            playerPosition.y += 0.2f * timestep * 0.02;
            a = 0.0f;
        }


        Renderer2D.drawClearQuad(
                playerPosition,
                0.0f,
                new Vector2f(0.1f, 0.1f),
                new Vector4f(1.0f, 0.0f, 0.0f,1.0f)
        );
    }
}
