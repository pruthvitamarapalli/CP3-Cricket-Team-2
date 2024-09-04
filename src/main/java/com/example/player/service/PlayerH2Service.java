package com.example.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.player.model.Player;
import com.example.player.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import com.example.player.model.PlayerRowMapper;
import java.util.*;

@Service
public class PlayerH2Service implements PlayerRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getPlayers() {
        List<Player> playerList = db.query("SELECT * from team", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override
    public Player addPlayer(Player player) {
        db.update("INSERT into team(playerName, jerseyNumber, role) values(?, ?, ?)", player.getPlayerName(),
                player.getJerseyNumber(), player.getRole());
        Player savedPlayer = db.queryForObject(
                "SELECT * from team WHERE playerName = ? and jerseyNumber = ?",
                new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber());
        return savedPlayer;
    }

    @Override
    public Player getPlayerById(int playerId) {
        try {
            Player player = db.queryForObject("SELECT * from team WHERE playerId = ?", new PlayerRowMapper(), playerId);
            return player;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Player updatePlayer(int playerId, Player player) {
        if (player.getPlayerName() != null) {
            db.update("Update team SET playername=? WHERE playerId = ?", player.getPlayerName(), playerId);
        }
        if (player.getJerseyNumber() != 0) {
            db.update("Update team SET jerseynumber=? WHERE playerId = ?", player.getJerseyNumber(), playerId);
        }
        if (player.getRole() != null) {
            db.update("Update team SET role=? WHERE playerId = ?", player.getRole(), playerId);
        }
        return getPlayerById(playerId);
    }

    @Override
    public void deletePlayer(int playerId) {
        db.update("Delete from team WHERE playerId = ?", playerId);
    }

}