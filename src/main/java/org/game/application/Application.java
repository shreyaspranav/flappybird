package org.game.application;

public interface Application {
    public void onStart();
    public void onUpdate(double timestep);
    public void onExit();
    public boolean isApplicationRunning();

}
