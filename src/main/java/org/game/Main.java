package org.game;

import org.game.application.ApplicationProperties;
import org.game.application.ApplicationRunner;

public class Main {
    private static final int WIDTH = 1600, HEIGHT = 900;
    private static final String TITLE = "FlappyBird Game";

    public static void main(String[] args) {
        ApplicationRunner app = new ApplicationRunner(new ApplicationProperties(WIDTH, HEIGHT, TITLE, false, false));
        app.run();
    }
}