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
    private String nickname;
    private boolean nicknameActive, nicknameEntered;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter, parameterLevel, parameterHighScore,
            parameterScoreAtual, parameterLeaderboard;
    private BitmapFont bitmap, bitmapLevel, bitmapHighScore, bitmapScoreAtual, bitmapLeaderboard;
    private DatabaseHelper actionSQL;

    private Rectangle botaoStart;
    private Rectangle botaoLoja;
    private Rectangle botaoRanking;
    private Rectangle botaoVoltar;
    private Rectangle nicknameBox;

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
        inimigos = new Array<>();
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
        parameter.color = new Color(255f / 255f, 204f / 255f, 0f / 255f, 1f);
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

        nickname = "";
        nicknameActive = false;
        nicknameEntered = false;
        nicknameBox = new Rectangle(540, 500, 360, 60);

        gameOver = false;
        gameStart = false;
        lojaStart = false;
        leaderboardStart = false;

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

        botaoStart = new Rectangle(558, 220, 332, 141);
        botaoLoja = new Rectangle(1217, 360, 166, 186);
        botaoRanking = new Rectangle(1259, 820, 100, 100);
    }

    @Override
    public void render() {
        batch.begin();

        if (!nicknameEntered) {
            ScreenUtils.clear(0, 0, 0, 1);
            bitmap.draw(batch, "Digite seu Nickname:", 540, 580);
            bitmap.draw(batch, nickname + (System.currentTimeMillis() % 1000 < 500 ? "|" : ""), nicknameBox.x + 10, nicknameBox.y + 40);

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !nickname.isEmpty()) {
                nicknameEntered = true;
            } else {
                for (int i = 0; i < 26; i++) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A + i)) {
                        if (nickname.length() < 7) {
                            nickname += (char) ('A' + i);
                        }
                    }
                }
                for (int i = 0; i < 10; i++) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0 + i)) {
                        if (nickname.length() < 7) {
                            nickname += (char) ('0' + i);
                        }
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && nickname.length() > 0) {
                    nickname = nickname.substring(0, nickname.length() - 1);
                }
            }

            batch.end();
            return;
        }

        batch.draw(homescreen, 0, 0);

        // Exibe o nickname no canto superior esquerdo
        bitmap.draw(batch, "Nick: " + nickname, 30, Gdx.graphics.getHeight() - 30);

        if (!gameStart) {
            if (!leaderboardStart && !lojaStart) {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    float mouseX = Gdx.input.getX();
                    float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
                    if (botaoStart.contains(mouseX, mouseY)) {
                        gameStart = true;
                    } else if (botaoLoja.contains(mouseX, mouseY)) {
                        lojaStart = true;
                    } else if (botaoRanking.contains(mouseX, mouseY)) {
                        leaderboardStart = true;
                    }
                }
            } else if (leaderboardStart) {
                batch.draw(leaderboard, 0, 0);
                List<Ranking> ranking = actionSQL.getRanking();
                float yOffset = 800;
                float startXNome = 540;
                int larguraNome = 20;
                float startXScore = 800;

                for (Ranking item : ranking) {
                    String nome = item.getNome();
                    int highscore = item.getHighscore();
                    String nomeFormatado = String.format("%-" + larguraNome + "s", nome);
                    bitmapLeaderboard.draw(batch, nomeFormatado, startXNome, yOffset);
                    bitmapLeaderboard.draw(batch, String.valueOf(highscore), startXScore, yOffset);
                    yOffset -= 50;
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    leaderboardStart = false;
                }
            } else if (lojaStart) {
                batch.draw(loja, 0, 0);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    lojaStart = false;
                }
            }
        } else {
            this.moveNave();
            this.moveBala();
            this.moveInimigos();

            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            batch.draw(image, 0, 0);

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
                bitmapLevel.draw(batch, "" + inimigosMortos, 1220, 685);
                bitmapLevel.draw(batch, "" + level, 1220, 585);
                bitmapHighScore.draw(batch, "" + actionSQL.getHighscore("Oplay"), 1220, 500);
                bitmapScoreAtual.draw(batch, "" + score, 550, 390);
                actionSQL.salvarJogador(nickname,score);

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
        homescreen.dispose();
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
            if (colisao(inimigo.x, inimigo.y, inimigo.width, inimigo.height, xBala, yBala, bala.getWidth(),
                    bala.getHeight()) && attack) {
                score += 5;
                ++inimigosMortos;
                if (score % 50 == 0) {
                    numInimigos -= 200;
                    ++level;
                }
                attack = false;
                iter.remove();
                xBala = posX;
                yBala = posY;
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