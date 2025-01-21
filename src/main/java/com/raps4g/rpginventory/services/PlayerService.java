package com.raps4g.rpginventory.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.raps4g.rpginventory.domain.entities.Player;
import com.raps4g.rpginventory.domain.entities.dto.PlayerDto;

public interface PlayerService {

    // Mappers
   
    Player mapFromPlayerDto(PlayerDto itemDto);

    PlayerDto mapToPlayerDto(Player item);

    // Add

    Player savePlayer(Player item);

    // Get

    Player getPlayer(Long playerId);
   
    Page<Player> getAllPlayers(Pageable pageable);
    
    // Delete

    void deletePlayer(Long itemId);
}
