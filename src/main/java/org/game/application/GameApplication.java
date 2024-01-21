package org.game.application;



public class GameApplication implements Application {

    private ApplicationProperties properties;
    public GameApplication(ApplicationProperties properties) { this.properties = properties; }

    @Override
    public void onStart() {
        System.out.println("Start!");
    }

    @Override
    public void onUpdate(double timestep) {
        System.out.println("Update " + timestep);
    }

    @Override
    public void onExit() {
        System.out.println("Exit");
    }
    @Override
    public boolean isApplicationRunning() {
        return true;
    }
}
