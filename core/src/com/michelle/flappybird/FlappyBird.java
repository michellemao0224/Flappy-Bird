package com.michelle.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


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
	//ShapeRenderer shapeRenderer;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	int score = 0;
	int scoringTube = 0;
	BitmapFont font;

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
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		for (int i = 0; i < numberOfTubes; i ++){
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap  - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth()+ i * distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}

		birdCircle = new Circle();
		//shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
	}

	@Override
	public void render () {// automatic loop
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(gameState != 0) {
			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score ++;
				Gdx.app.log("Score", String.valueOf(score));
				if(scoringTube < numberOfTubes - 1){
					scoringTube ++;
				}else{
					scoringTube = 0;
				}
			}
			if(Gdx.input.justTouched()){
				//Gdx.app.log("Touched","Yep!");
				velocity = -25;

			}
			for (int i = 0; i < numberOfTubes; i ++) {
				if(tubeX[i] < - toptube.getWidth()){
					// check if the tube is fully disappeared from the screen
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					//randomGenerator.nextFloat()---- value is 0 ~ 1
					//randomGenerator.nextFloat() - 0.5f     value is 0 ~ 0.5
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap  - 200);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);
				//create rectangle to overlap each top or bottom tube
				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(), bottomtube.getHeight());
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
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();
		//create a circle to wrap the bird
		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flappyState].getHeight() / 2, birds[flappyState].getWidth() / 2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i ++){
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
			//shapeRenderer.rect(tubeX[i],
					//Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(), bottomtube.getHeight());
			//check whether circle and rectangle overlaps
			if(Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){
				Gdx.app.log("Collision", "Yup!");
		}
		}
		//shapeRenderer.end();
	}
	
//	@Override
//	public void dispose () {
//		batch.dispose();
//		background.dispose();
//	}
}
