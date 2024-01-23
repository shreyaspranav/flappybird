package org.game.application;

import java.time.Duration;
import java.time.Instant;

public class ApplicationRunner {
    private ApplicationProperties properties;

    public ApplicationRunner(ApplicationProperties properties) { this.properties = properties; }
    public void run() {
        Application app = new GameApplication(properties);
        app.onStart();

        double ts = 0.0f;
        while (app.isApplicationRunning()) {
            Instant start = Instant.now();

            app.onUpdate(ts);
            System.out.println(ts);

            Instant end = Instant.now();

            ts = (double) Duration.between(start, end).toNanos() / 1000000.0;
        }

        app.onExit();

    }
}
