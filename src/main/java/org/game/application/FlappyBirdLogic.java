package org.game.application;

import org.game.engine.Camera2D;
import org.game.engine.RenderCommand;
import org.joml.Vector3f;

public class FlappyBirdLogic {

    private Camera2D sceneCamera;
    private Vector3f cameraPosition;

    public void start() {
        cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        sceneCamera = new Camera2D(cameraPosition, 1600, 900); // TEMP: Change it!
    }

    public void update(double timestep) {
        RenderCommand.ClearScreen(0.1f, 0.1f, 0.1f, 1.0f);
        sceneCamera.update();

        updateGame(timestep);
    }

    private void updateGame(double timestep) {

    }
}
