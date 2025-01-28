package com.raps4g.rpginventory.controllers;

import java.util.List;
import java.util.stream.Collectors;

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

import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.dto.PlayerDto;
import com.raps4g.rpginventory.services.JwtService;
import com.raps4g.rpginventory.services.PlayerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private JwtService jwtService;

    // POST

    @PostMapping(path = "/players")
    public ResponseEntity<PlayerDto> createPlayer(
        HttpServletRequest http,
        @Valid @RequestBody PlayerDto playerDto
    ) {

        String authHeader = http.getHeader("Authorization");
        String token = jwtService.extractToken(authHeader);
        User user = jwtService.extractUser(token);
        
        Player player = playerService.mapFromPlayerDto(playerDto);
        player.setGold(1000L);
        player.setUser(user); 
        Player savedPlayer = playerService.savePlayer(player);
        PlayerDto savedPlayerDto = playerService.mapToPlayerDto(savedPlayer);

        return new ResponseEntity<>(savedPlayerDto, HttpStatus.CREATED);
    }

    // GET

    @GetMapping(path = {"/admin/players"})
    public ResponseEntity<Page<PlayerDto>> getAllPlayers(Pageable pageable) {

        Page<Player> players = playerService.getAllPlayers(pageable);

        return new ResponseEntity<>(players.map(playerService::mapToPlayerDto),HttpStatus.OK);
    }
    
    @GetMapping(path = {"/players"})
    public ResponseEntity<List<PlayerDto>> getAllPlayersForCurrentUser(
        HttpServletRequest http
    ) {
        
        String authHeader = http.getHeader("Authorization");
        String token = jwtService.extractToken(authHeader);
        User user = jwtService.extractUser(token);
        Long userId = user.getId();

        List<Player> players = playerService.getAllPlayersForCurrentUser(userId);
        List<PlayerDto> playersDto = players.stream()
            .map(playerService::mapToPlayerDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(playersDto, HttpStatus.OK);
    }

    @GetMapping(path = {"/players/{playerId}"})
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable Long playerId) {

        Player foundPlayer = playerService.getPlayer(playerId);
        PlayerDto foundPlayerDto = playerService.mapToPlayerDto(foundPlayer);
        return new ResponseEntity<>(foundPlayerDto, HttpStatus.OK);
    }


    //DELETE

    @DeleteMapping(path = "/admin/players/{playerId}")
    public ResponseEntity deletePlayer(@PathVariable Long playerId) {

        playerService.deletePlayer(playerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
