package com.raps4g.rpginventory.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.domain.entities.Player;
import com.raps4g.rpginventory.domain.entities.dto.PlayerDto;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.exceptions.ResourceNotFoundException;
import com.raps4g.rpginventory.repositories.InventoryRepository;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.services.PlayerService;


@Service
public class PlayerServiceImpl implements PlayerService{
   
    @Autowired 
    private ModelMapper modelMapper;
    
    @Autowired 
    private PlayerRepository playerRepository;
    

    // Mappers

    @Override
    public Player mapFromPlayerDto(PlayerDto playerDto) {
        return modelMapper.map(playerDto, Player.class);
    }

    @Override
    public PlayerDto mapToPlayerDto(Player player) {
        return modelMapper.map(player, PlayerDto.class);
    }


    // Add

    @Override
    public Player savePlayer(Player player) {

        if (playerRepository.findByName(player.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Player named \"" + player.getName() + "\" already exists.");
        }
        
        return playerRepository.save(player);
    }


    // Get

   @Override
    public Page<Player> getAllPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Override
    public Player getPlayer(Long playerId) {
        return playerRepository.findById(playerId)
        .orElseThrow(() -> new ResourceNotFoundException("Player with id: " + playerId + " not found"));
    }


    // Delete

    @Override
    public void deletePlayer(Long playerId) {
        playerRepository.deleteById(playerId);
    }
    
}
