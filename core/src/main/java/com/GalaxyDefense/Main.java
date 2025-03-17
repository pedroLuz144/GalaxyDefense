package com.GalaxyDefense;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image, tNave, tituloPequeno, exit, trofeu, side, score;
    private Sprite nave;
    private float posX, posY, velocity;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("background.png");
        tNave = new Texture("nave.png");
        nave = new Sprite(tNave); 
        posX = 720 - 190;
        posY = 50;
        velocity = 10;
        
        //Adições próprias
        tituloPequeno = new Texture("tituloPequeno.png");
        exit = new Texture("exit.png");
        trofeu = new Texture("trofeu.png");
        side = new Texture("side.png");
        score = new Texture("score.png");

    }

    @Override
    public void render() {
        this.moveNave();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 0, 0);
        batch.draw(nave, posX, posY);
        
        //Adições próprias
        batch.draw(tituloPequeno, 1113, 464);
        batch.draw(exit, 1167, 247);
        batch.draw(trofeu, 1318, 897);        
        batch.draw(side, 0, 0);        
        batch.draw(side, 1090, 0);        
        batch.draw(score, 1160, 650);        
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        tNave.dispose();
    }

    private void moveNave() {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(posX < 1440 - 88 - 355) {
                posX += velocity;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(posX > 359) {
                posX -= velocity;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(posY < 200) {
                posY += velocity;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(posY > 20) {
                posY -= velocity;
            }
        }
    }
}
