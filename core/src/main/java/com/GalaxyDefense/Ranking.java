package com.GalaxyDefense;

public class Ranking {
    private String nome;
    private int highscore;

    public Ranking(String nome, int highscore) {
        this.nome = nome;
        this.highscore = highscore;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }
}