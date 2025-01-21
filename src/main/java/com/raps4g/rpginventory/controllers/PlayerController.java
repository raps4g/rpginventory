package com.raps4g.rpginventory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // POST
    
    @PostMapping(path = "/players")
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {

            Player player = playerService.mapFromPlayerDto(playerDto);
            Player savedPlayer = playerService.savePlayer(player);
            PlayerDto savedPlayerDto = playerService.mapToPlayerDto(savedPlayer);
        
            return new ResponseEntity<>(savedPlayerDto, HttpStatus.CREATED);
    }
   
    
    // PUT

    @PutMapping(path = "/players/{playerId}")
    public ResponseEntity<PlayerDto> updatePlayer(
        @PathVariable Long playerId, 
        @RequestBody PlayerDto playerDto
        ) {

            Player player = playerService.mapFromPlayerDto(playerDto);
            player.setId(playerId);
            Player savedPlayer = playerService.savePlayer(player);
            PlayerDto savedPlayerDto = playerService.mapToPlayerDto(savedPlayer);

            return new ResponseEntity<>(savedPlayerDto, HttpStatus.OK);
    }

    
    // GET

    @GetMapping(path = "/players")
    public Page<PlayerDto> getAllPlayers(Pageable pageable) {

        Page<Player> players = playerService.getAllPlayers(pageable);

        return players.map(playerService::mapToPlayerDto);
    }

    @GetMapping(path = "/players/{playerId}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable Long playerId) {

        Player foundPlayer = playerService.getPlayer(playerId);
        PlayerDto foundPlayerDto = playerService.mapToPlayerDto(foundPlayer);
        return new ResponseEntity<>(foundPlayerDto, HttpStatus.OK);
    }

    
    //DELETE

    @DeleteMapping(path = "/players/{playerId}")
    public ResponseEntity deletePlayer(@PathVariable Long playerId) {

        playerService.deletePlayer(playerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
