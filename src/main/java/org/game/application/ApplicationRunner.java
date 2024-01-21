package org.game.application;

public class ApplicationRunner {
    private ApplicationProperties properties;

    public ApplicationRunner(ApplicationProperties properties) { this.properties = properties; }
    public void run() {
        Application app = new GameApplication(properties);
        app.onStart();

        while (app.isApplicationRunning()) {
            app.onUpdate(0.0);
        }

        app.onExit();

    }
}
