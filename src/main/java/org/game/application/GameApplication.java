package org.game.application;

import org.game.engine.Window;

public class GameApplication implements Application {

    private ApplicationProperties properties;
    private Window window;

    public GameApplication(ApplicationProperties properties) { this.properties = properties; }

    @Override
    public void onStart() {
        System.out.println("Start!");
        window = new Window(properties);
        window.createWindow();
    }

    @Override
    public void onUpdate(double timestep) {
        System.out.println("Update " + timestep);
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
