package com.michelle.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

import java.util.Random;


public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;

	Texture toptube;
	Texture bottomtube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;

	//Create gravity system
	float birdY = 0;
	float velocity = 0;
	float gravity = 2;

	int flappyState = 0;
	int gameState = 0;

	int numberOfTubes = 4;
	float tubeVelocity = 6;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;

	Circle birdCircle;
	ShapeRenderer shapeRenderer;
 	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		randomGenerator = new Random();
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

		for (int i = 0; i < numberOfTubes; i ++){
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap  - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + i * distanceBetweenTubes;
		}

		birdCircle = new Circle();
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {// automatic loop
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(gameState !=0) {
			if(Gdx.input.justTouched()){
				//Gdx.app.log("Touched","Yep!");
				velocity = -25;
				//randomGenerator.nextFloat()---- value is 0 ~ 1
				//randomGenerator.nextFloat() - 0.5f     value is 0 ~ 0.5

			}
			for (int i = 0; i < numberOfTubes; i ++) {
				if(tubeX[i] < - toptube.getWidth()){
					// check if the tube is fully disappeared from the screen
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap  - 200);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);
			}
			if(birdY > 0 || velocity < 0) {
				//Create gravity system
				velocity = velocity + gravity;
				birdY -= velocity;
			}
		} else{
			if(Gdx.input.justTouched()){
				//Gdx.app.log("Touched","Yep!");
				gameState = 1;//touch the screen & restart the game
			}
		}
		if (flappyState == 0) {
			flappyState = 1;
		} else {
			flappyState = 0;
		}

		batch.draw(birds[flappyState], Gdx.graphics.getWidth() / 2 - birds[flappyState].getWidth() / 2, birdY);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flappyState].getHeight() / 2, birds[flappyState].getWidth() / 2);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		shapeRenderer.end();
	}
	
//	@Override
//	public void dispose () {
//		batch.dispose();
//		background.dispose();
//	}
}
