package org.game.application;

import org.game.engine.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class GameApplication implements Application {

    private ApplicationProperties properties;
    private Window window;

    private FlappyBirdLogic gameLogic;

    public GameApplication(ApplicationProperties properties) { this.properties = properties; }

    @Override
    public void onStart() {
        //System.out.println("Start!");
        window = new Window(properties);
        window.createWindow();

        gameLogic = new FlappyBirdLogic();
        gameLogic.start(window);

        Renderer2D.init();
    }

    @Override
    public void onUpdate(double timestep) {

        gameLogic.update(timestep);

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
