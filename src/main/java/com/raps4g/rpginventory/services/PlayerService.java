package com.raps4g.rpginventory.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.dto.PlayerDto;

public interface PlayerService {

    // Mappers
   
    Player mapFromPlayerDto(PlayerDto itemDto);

    PlayerDto mapToPlayerDto(Player player);

    // Add

    Player savePlayer(Player player);

    // Get

    Player getPlayer(Long playerId);
   
    Page<Player> getAllPlayers(Pageable pageable);
    
    // Delete

    void deletePlayer(Long itemId);
}
