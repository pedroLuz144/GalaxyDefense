package com.GalaxyDefense;

import java.sql.Time;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image, tNave, tBala, tInimigo, tituloPequeno, exit, trofeu, side, score;
    private Sprite nave, bala;
    private float posX, posY, velocity, xBala, yBala;
    private boolean attack;
    private Array<Rectangle> inimigos;
    private long tempoUltimoInimigo;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("background.png");
        tNave = new Texture("nave.png");
        nave = new Sprite(tNave); 
        posX = 720 - 190;
        posY = 50;
        velocity = 10;
        
        tBala = new Texture("bala.png");
        bala = new Sprite(tBala);
        xBala = posX;
        yBala = posY;
        attack = false;
        
        tInimigo = new Texture("inimigo.png");
        inimigos = new Array<Rectangle>();
        tempoUltimoInimigo = 0;
        
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
        this.moveBala();
        this.moveInimigos();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 0, 0);
        if(attack){
            batch.draw(bala, xBala + nave.getWidth() / 2 - 16, yBala + nave.getHeight() / 2 - 17);
        }
        batch.draw(nave, posX, posY);

        for(Rectangle inimigo : inimigos) {
            batch.draw(tInimigo, inimigo.x, inimigo.y);
        }
        
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

    private void moveBala() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !attack) {
            attack = true;
            xBala = posX;
        }
        
        if(attack) {
            if(yBala < Gdx.graphics.getWidth()) {
                yBala += 15;
            } else {
                xBala = posX;
                yBala = posY;
                attack = false;    
            }
        } else {
            xBala = posX;
            yBala = posY;
        }
    }

    private void spawnInimigos() {
        Rectangle inimigo = new Rectangle(MathUtils.random(355, 1006), 1024, tInimigo.getWidth(), tInimigo.getHeight());
        inimigos.add(inimigo);
        tempoUltimoInimigo = TimeUtils.nanoTime();
    }

    private void moveInimigos() {
        if(TimeUtils.nanoTime() - tempoUltimoInimigo > 999999999) {
            this.spawnInimigos();
        }
        for(Iterator<Rectangle> iter = inimigos.iterator(); iter.hasNext();) {
            Rectangle inimigo = iter.next();
            inimigo.y -= 400 * Gdx.graphics.getDeltaTime(); 
            if(inimigo.y < 0) {
                iter.remove();
            }
        }
    }
}
