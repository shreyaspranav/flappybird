package org.game.application;

import org.game.engine.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Random;

public class FlappyBirdLogic {

    private Camera2D sceneCamera;
    private Vector3f cameraPosition;
    private Vector3f playerPosition;

    private Texture birdMidFlap;
    private Texture birdUpFlap;
    private Texture birdDownFlap;

    private Texture pillarGreen;
    private Texture pillarRed;

    private Texture gameOverTexture;

    private Texture[] numbers;

    private Texture backgroundDay;
    private Texture backgroundNight;

    private float[] randomNumbers;
    private int[] scoreDecimal;

    private int score = 0;

    private float d;

    private float pillarGap = 0.5f;

    private boolean gameOver = false;
    private float gameOverElapsed = 0.0f;

    // physics properties:
    private final float GRAVITY = 15.0f;

    public void start(Window window) {
        cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        sceneCamera = new Camera2D(cameraPosition, window.getProperties().getWindowWidth(), window.getProperties().getWindowHeight());

        playerPosition = new Vector3f(0.0f, 0.0f, 0.0f);

        birdMidFlap = new Texture("textures/bluebird-midflap.png", true);
        birdDownFlap = new Texture("textures/bluebird-downflap.png", true);
        birdUpFlap = new Texture("textures/bluebird-upflap.png", true);

        pillarGreen = new Texture("textures/pipe-green.png", true);
        pillarRed = new Texture("textures/pipe-red.png", true);

        gameOverTexture = new Texture("textures/gameover.png", true);

        backgroundDay = new Texture("textures/background-day.png", true);
        backgroundNight = new Texture("textures/background-night.png", true);

        numbers = new Texture[10];

        for(int i = 0; i < 10; i++)
            numbers[i] = new Texture("textures/" + i + ".png", true);

        Renderer2D.init();
        Input.setWindow(window);

        randomNumbers = new float[20];

        Random r = new Random();

        for (int i = 0; i < 20; i++)
            randomNumbers[i] = r.nextFloat();

        scoreDecimal = new int[4];
    }

    public void update(double timestep) {
        RenderCommand.ClearScreen(0.1f, 0.1f, 0.1f, 1.0f);

        Renderer2D.beginScene(sceneCamera);
        updateGame(timestep);
        Renderer2D.endScene();
    }

    private void updateGame(double timestep) {

        if(!gameOver) {
            cameraPosition.x += timestep * 0.001f;
            playerPosition.x = cameraPosition.x - 1.2f;
        }

        sceneCamera.setPosition(cameraPosition);

        Texture currentBirdTexture = birdMidFlap;
        if(d > 4.0f)
            currentBirdTexture = birdUpFlap;

        playerPosition.y -= d * timestep * 0.0001f;
        d += GRAVITY * timestep * 0.0001f * 7.0f;

//        System.out.println(playerPosition.y);

        if(Input.isKeyPressed(KeyCode.KEY_SPACE)) {
            playerPosition.y += timestep * 0.001f * 2.5f;
            d = 0.0f;

            currentBirdTexture = birdDownFlap;
        }

        // Background:
        {
            for (int i = score < 4 ? 0 : score - 4; i < score + 10; i++) {
                Renderer2D.drawTexturedQuad(
                        new Vector3f(2.0f * (float) backgroundNight.getTextureWidth() / (float) backgroundNight.getTextureHeight() * i - 3.0f, 0.0f, 0.0f),
                        0.0f,
                        new Vector2f(2.0f * (float) backgroundNight.getTextureWidth() / (float) backgroundNight.getTextureHeight(), 2.0f),
                        backgroundDay
                );
            }
        }

        // Player:
        {
            Renderer2D.drawTexturedQuad(
                    playerPosition,
                    0.0f,
                    new Vector2f(0.1f * (float) currentBirdTexture.getTextureWidth() / (float) currentBirdTexture.getTextureHeight(), 0.1f),
                    currentBirdTexture
            );
        }

        // Pillars:
        {
            for (int i = score < 4 ? 0 : score - 4; i < score + 10; i++) {

                Renderer2D.drawTexturedQuad(
                        new Vector3f(1.0f * i, randomNumbers[i % 20] - 0.5f - pillarGap, 0.1f),
                        0.0f,
                        new Vector2f(1.5f * (float) pillarGreen.getTextureWidth() / (float) pillarGreen.getTextureHeight(), 1.5f),
                        pillarGreen
                );

                Renderer2D.drawTexturedQuad(
                        new Vector3f(1.0f * i, randomNumbers[i % 20] + 0.5f + pillarGap, 0.1f),
                        0.0f,
                        new Vector2f(1.5f * (float) pillarGreen.getTextureWidth() / (float) pillarGreen.getTextureHeight(), -1.5f),
                        pillarGreen
                );
            }
        }

        // Collision detection
        {
            Vector3f topPillarPosition = new Vector3f(Math.max(0, score), randomNumbers[Math.max(0, score) % 20] + 0.5f + pillarGap, 0.1f);
            Vector3f bottomPillarPosition = new Vector3f(Math.max(0, score), randomNumbers[Math.max(0, score) % 20] - 0.5f - pillarGap, 0.1f);

            Vector2f pillarScale = new Vector2f(1.5f * (float) pillarGreen.getTextureWidth() / (float) pillarGreen.getTextureHeight(), 1.5f);
            Vector2f playerScale = new Vector2f(0.1f * (float) currentBirdTexture.getTextureWidth() / (float) currentBirdTexture.getTextureHeight(), 0.1f);

            if(isQuadCollided(playerPosition, playerScale, topPillarPosition, pillarScale) || isQuadCollided(playerPosition, playerScale, bottomPillarPosition, pillarScale)
            || playerPosition.y > 2.0f || playerPosition.y < -2.0f) {
                gameOver = true;
            }
        }

        // Score:
        {
            int tempScore = Math.max(score, 0);
            for(int i = scoreDecimal.length - 1; i >= 0; i--) {
                scoreDecimal[i] = tempScore % 10;
                tempScore /= 10;
            }

            for(int i = 0; i < scoreDecimal.length; i++) {
                Renderer2D.drawTexturedQuad(
                        new Vector3f(cameraPosition.x + i * 0.1f * (float) numbers[0].getTextureWidth() / (float) numbers[0].getTextureHeight(), 0.9f,0.0f),
                        0.0f,
                        new Vector2f(0.1f * (float) numbers[0].getTextureWidth() / (float) numbers[0].getTextureHeight(), 0.1f),
                        numbers[scoreDecimal[i]]
                );
            }
        }

        // Score Calculation:
        score = (int)(cameraPosition.x - 1.5f * (float) pillarGreen.getTextureWidth() / (float) pillarGreen.getTextureHeight());

        if(gameOver) {
            gameOverElapsed += timestep;

            if(gameOverElapsed > 500.0f) {
                Renderer2D.drawTexturedQuad(
                        cameraPosition,
                        0.0f,
                        new Vector2f(0.6f * (float)gameOverTexture.getTextureWidth() / (float)gameOverTexture.getTextureHeight(), 0.6f),
                        gameOverTexture
                );

                if(Input.isKeyPressed(KeyCode.KEY_ENTER)) {
                    gameOver = false;
                    gameOverElapsed = 0.0f;

                    cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);
                    playerPosition = new Vector3f(cameraPosition.x - 1.2f, 0.0f, 0.0f);

                    d = 0.0f;
                }
            }
        }
    }

    private boolean isQuadCollided(Vector3f position_1, Vector2f scale_1, Vector3f position_2, Vector2f scale_2)
    {
        boolean cx, cy;

        if (position_2.x > position_1.x)
            cx = position_2.x - position_1.x <= (scale_1.x + scale_2.x) / 2.0f;
        else
            cx = position_1.x - position_2.x <= (scale_1.x + scale_2.x) / 2.0f;

        if (position_2.y > position_1.y)
            cy = position_2.y - position_1.y <= (scale_1.y + scale_2.y) / 2.0f;
        else
            cy = position_1.y - position_2.y <= (scale_1.y + scale_2.y) / 2.0f;

        return cx && cy;
    }

}
