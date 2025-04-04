package com.GalaxyDefense;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/galaxy_defense?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Carrega o driver JDBC
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado!", e);
        }
    }

    public int getHighscore(String jogadorId) {
        int score = 0;
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT highscore FROM ranking WHERE nomeR = ?")) {
            preparedStatement.setString(1, jogadorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                score = resultSet.getInt("highscore");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return score;
    }

    public List<Ranking> getRanking() {
        List<Ranking> rankingList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT nomeR, highscore FROM ranking ORDER BY highscore DESC LIMIT 5")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nome = resultSet.getString("nomeR");
                int highscore = resultSet.getInt("highscore");
                rankingList.add(new Ranking(nome, highscore));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rankingList;
    }

    public void salvarJogador(String nome, int pontuacao) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement checkPlayerStatement = connection.prepareStatement("SELECT COUNT(*) FROM player WHERE nome = ?")) {
                checkPlayerStatement.setString(1, nome);
                ResultSet playerResult = checkPlayerStatement.executeQuery();
                playerResult.next();
                int playerCount = playerResult.getInt(1);
    
                if (playerCount == 0) {
                    try (PreparedStatement insertPlayerStatement = connection.prepareStatement("INSERT INTO player (nome, score) VALUES (?, ?)")) {
                        insertPlayerStatement.setString(1, nome);
                        insertPlayerStatement.setInt(2, pontuacao);
                        insertPlayerStatement.executeUpdate();
                    }
                    try (PreparedStatement insertRankingStatement = connection.prepareStatement("INSERT INTO ranking (nomeR, highscore) VALUES (?, ?)")) {
                        insertRankingStatement.setString(1, nome);
                        insertRankingStatement.setInt(2, pontuacao);
                        insertRankingStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement checkRankingStatement = connection.prepareStatement("SELECT highscore FROM ranking WHERE nomeR = ?")) {
                        checkRankingStatement.setString(1, nome);
                        ResultSet rankingResult = checkRankingStatement.executeQuery();
    
                        if (rankingResult.next()) {
                            int currentHighscore = rankingResult.getInt("highscore");
                            if (pontuacao > currentHighscore) {
                                try (PreparedStatement updateRankingStatement = connection.prepareStatement("UPDATE ranking SET highscore = ? WHERE nomeR = ?")) {
                                    updateRankingStatement.setInt(1, pontuacao);
                                    updateRankingStatement.setString(2, nome);
                                    updateRankingStatement.executeUpdate();
                                }
                            }
                        } else {
                            try (PreparedStatement insertRankingStatement = connection.prepareStatement("INSERT INTO ranking (nomeR, highscore) VALUES (?, ?)")) {
                                insertRankingStatement.setString(1, nome);
                                insertRankingStatement.setInt(2, pontuacao);
                                insertRankingStatement.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}