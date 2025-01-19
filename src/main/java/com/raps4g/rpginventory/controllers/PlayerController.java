package com.raps4g.rpginventory.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.domain.entities.Player;
import com.raps4g.rpginventory.domain.entities.dto.PlayerDto;
import com.raps4g.rpginventory.services.PlayerService;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class PlayerController {
    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // POST

    @PostMapping(path = "/players")
    public ResponseEntity<PlayerDto> addPlayer(@RequestBody PlayerDto playerDto) {
            Player player = playerService.convertFromPlayerDto(playerDto);
            Player savedPlayer = playerService.savePlayer(player);
            PlayerDto savedPlayerDto = playerService.convertToPlayerDto(savedPlayer);
            return new ResponseEntity<>(savedPlayerDto, HttpStatus.CREATED);
    }
    

    // PUT
   
    @PutMapping(path = "/players/{playerId}")
    public ResponseEntity<PlayerDto> uptadePlayer(@PathVariable Long playerId, 
        @RequestBody PlayerDto playerDto) {
            Player player = playerService.convertFromPlayerDto(playerDto);
            player.setId(playerId);
            Player savedPlayer = playerService.savePlayer(player);
            PlayerDto savedPlayerDto = playerService.convertToPlayerDto(savedPlayer);
            return new ResponseEntity<>(savedPlayerDto, HttpStatus.OK);
    }


    // GET

    @GetMapping(path = "/players")
    public Page<PlayerDto> getAllPlayers( Pageable pageable) {

        Page<Player> players = playerService.getAllPlayers(pageable);

        return players.map(playerService::convertToPlayerDto);
    }

    @GetMapping(path = "/players/{playerId}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable Long playerId) {
        Optional<Player> foundPlayer = playerService.getPlayer(playerId);
        return foundPlayer.map(player -> {
            PlayerDto playerDto = playerService.convertToPlayerDto(player);
            return new ResponseEntity<>(playerDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/players/{playerId}")
    public ResponseEntity deletePlayer(@PathVariable Long playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
