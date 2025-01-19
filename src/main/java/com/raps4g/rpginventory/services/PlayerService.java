package com.raps4g.rpginventory.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.raps4g.rpginventory.domain.entities.Player;
import com.raps4g.rpginventory.domain.entities.dto.PlayerDto;

public interface PlayerService {

    // Mappers
   
    Player convertFromPlayerDto(PlayerDto itemDto);

    PlayerDto convertToPlayerDto(Player item);

    // Add

    Player savePlayer(Player item);

    // Get

    Optional<Player> getPlayer(Long playerId);
   
    List<Player> getAllPlayers();
    
    Page<Player> getAllPlayers(Pageable pageable);
    
    // Delete

    void deletePlayer(Long itemId);
    
}
