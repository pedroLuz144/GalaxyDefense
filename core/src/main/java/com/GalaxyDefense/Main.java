package com.GalaxyDefense;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image, tNave, tBala, tInimigo, tituloPequeno, exit, trofeu, side, levelImg, scoreImg, telaGameOver,
            homescreen, loja, leaderboard;
    private Sprite nave, bala;
    private float posX, posY, velocity, xBala, yBala;
    private boolean attack, gameOver, gameStart, lojaStart, leaderboardStart;
    private Array<Rectangle> inimigos;
    private long tempoUltimoInimigo;
    private int score, power, numInimigos, level, highScore, inimigosMortos;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter, parameterLevel, parameterHighScore,
            parameterScoreAtual, parameterLeaderboard;
    private BitmapFont bitmap, bitmapLevel, bitmapHighScore, bitmapScoreAtual, bitmapLeaderboard;
    private DatabaseHelper actionSQL;

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

        score = 0;
        inimigosMortos = 0;
        power = 1;
        numInimigos = 899999999;
        level = 1;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        parameter.color = new Color(255f / 255f, 204f / 255f, 0f / 255f, 1f); // FFCC00 (Amarelo)
        bitmap = generator.generateFont(parameter);

        parameterLevel = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 58;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.WHITE;
        bitmapLevel = generator.generateFont(parameter);

        parameterHighScore = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 58;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        parameter.color = new Color(225f / 255f, 255f / 255f, 0f / 255f, 1f);
        bitmapHighScore = generator.generateFont(parameter);

        parameterScoreAtual = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 156;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        parameter.color = new Color(255f / 255f, 31f / 255f, 31f / 255f, 1f);
        bitmapScoreAtual = generator.generateFont(parameter);

        parameterLeaderboard = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.WHITE;
        bitmapLeaderboard = generator.generateFont(parameter);

        gameOver = false;
        gameStart = false;
        lojaStart = false;
        leaderboardStart = false;

        // Adições próprias
        tituloPequeno = new Texture("tituloPequeno.png");
        exit = new Texture("exit.png");
        trofeu = new Texture("trofeu.png");
        side = new Texture("side.png");
        levelImg = new Texture("levelImg.png");
        scoreImg = new Texture("scoreImg.png");
        telaGameOver = new Texture("tela_gameover.png");
        homescreen = new Texture("homescreen.png");
        loja = new Texture("loja.png");
        leaderboard = new Texture("leaderboard.png");
        actionSQL = new DatabaseHelper();

    }

    @Override
    public void render() {
        batch.begin();

        if (!gameStart) {
            batch.draw(homescreen, 0, 0);

            if (!leaderboardStart) {
                if (Gdx.input.isKeyPressed(Input.Keys.T)) {
                    leaderboardStart = true;
                }
            } else {
                batch.draw(leaderboard, 0, 0);
                List<Ranking> ranking = actionSQL.getRanking();
                float yOffset = 800;
                float startXNome = 540; // Posição X inicial para o nome
                int larguraNome = 20; // Largura fixa para a coluna do nome (ajuste conforme necessário)
                float startXScore = 800; // Posição X onde a coluna do score deve começar (ajuste conforme necessário)

                for (Ranking item : ranking) {
                    String nome = item.getNome();
                    int highscore = item.getHighscore();

                    // Formata o nome com largura fixa e alinhamento à esquerda
                    String nomeFormatado = String.format("%-" + larguraNome + "s", nome);

                    // Desenha o nome formatado na sua posição
                    bitmapLeaderboard.draw(batch, nomeFormatado, startXNome, yOffset);

                    // Desenha o score na posição fixa para a coluna de score
                    bitmapLeaderboard.draw(batch, String.valueOf(highscore), startXScore, yOffset);

                    yOffset -= 50;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                    leaderboardStart = false;
                }
            }

            if (!lojaStart) {
                if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                    lojaStart = true;
                }
            } else {
                batch.draw(loja, 0, 0);
                if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                    lojaStart = false;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                gameStart = true;
            }
        } else {
            this.moveNave();
            this.moveBala();
            this.moveInimigos();

            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            batch.draw(image, 0, 0);

            // Adições próprias
            batch.draw(tituloPequeno, 1113, 464);
            batch.draw(exit, 1167, 247);
            batch.draw(trofeu, 1318, 897);
            batch.draw(side, 0, 0);
            batch.draw(side, 1090, 0);
            batch.draw(levelImg, 35, 650);
            batch.draw(scoreImg, 1160, 650);

            if (!gameOver) {
                if (attack) {
                    batch.draw(bala, xBala + nave.getWidth() / 2 - 16, yBala + nave.getHeight() / 2 - 17);
                }
                batch.draw(nave, posX, posY);

                for (Rectangle inimigo : inimigos) {
                    batch.draw(tInimigo, inimigo.x, inimigo.y);
                }
                bitmap.draw(batch, "" + score, 1240, 735);
                bitmap.draw(batch, "" + level, 130, 735);
            } else {
                batch.draw(telaGameOver, 0, 0);
                // Inimigos destruídos
                bitmapLevel.draw(batch, "" + inimigosMortos, 1220, 685);

                // Level
                bitmapLevel.draw(batch, "" + level, 1220, 585);

                // High Score
                bitmapHighScore.draw(batch, "" + actionSQL.getHighscore("Oplay"), 1220, 500);

                // Score da partida atual
                bitmapScoreAtual.draw(batch, "" + score, 550, 390);

                if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    score = 0;
                    inimigosMortos = 0;
                    power = 1;
                    level = 1;
                    posX = 720 - 190;
                    posY = 50;
                    inimigos.clear();
                    gameOver = false;
                    tempoUltimoInimigo = 0;
                }

            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        tNave.dispose();
    }

    private void moveNave() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (posX < 1440 - 88 - 355) {
                posX += velocity;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (posX > 359) {
                posX -= velocity;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (posY < 200) {
                posY += velocity;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (posY > 20) {
                posY -= velocity;
            }
        }
    }

    private void moveBala() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !attack) {
            attack = true;
            xBala = posX;
        }

        if (attack) {
            if (yBala < Gdx.graphics.getWidth()) {
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
        if (TimeUtils.nanoTime() - tempoUltimoInimigo > numInimigos) {
            this.spawnInimigos();
        }
        for (Iterator<Rectangle> iter = inimigos.iterator(); iter.hasNext();) {
            Rectangle inimigo = iter.next();
            inimigo.y -= 400 * Gdx.graphics.getDeltaTime();
            // Colisão com a bala
            if (colisao(inimigo.x, inimigo.y, inimigo.width, inimigo.height, xBala, yBala, bala.getWidth(),
                    bala.getHeight()) && attack) {
                score += 5;
                ++inimigosMortos;
                if (score % 15 == 0) {
                    numInimigos -= 100;
                    ++level;
                }
                attack = false;
                iter.remove();
                xBala = posX;
                yBala = posY;
                // Colisão com a nave
            } else if (colisao(inimigo.x, inimigo.y, inimigo.width, inimigo.height, posX, posY, nave.getWidth(),
                    nave.getHeight()) && !gameOver) {
                --power;
                if (power <= 0) {
                    gameOver = true;
                }
                iter.remove();
            }
            if (inimigo.y < 0) {
                iter.remove();
            }
        }
    }

    private boolean colisao(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
        if (x1 + w1 > x2 && x1 < x2 + w2 && y1 + h1 > y2 && y1 < y2 + h2) {
            return true;
        }
        return false;
    }

}
