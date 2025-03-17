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
    private Texture image, tNave;
    private Sprite nave;
    private float posX, posY, velocity;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("background.png");
        tNave = new Texture("nave.png");
        nave = new Sprite(tNave); //A imagem é maior que o tamanho da navinha em si(ela tem 80 'pixeis vazios' para a direita e 190 para a esquerda), 
                                  // considerar o tamanho dela como 80x85
        posX = 720 - 190;
        posY = 0;
        velocity = 10;
    }

    @Override
    public void render() {
        this.moveNave();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 0, 0);
        batch.draw(nave, posX, posY);
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
            if(posX < 1440 - 190 - 310) {
                posX += velocity;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(posX > -80 + 310) {
                posX -= velocity;
            }
        }
    }
}
