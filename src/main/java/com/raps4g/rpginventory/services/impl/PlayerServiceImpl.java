package com.raps4g.rpginventory.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.domain.entities.Player;
import com.raps4g.rpginventory.domain.entities.dto.PlayerDto;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.services.PlayerService;


@Service
public class PlayerServiceImpl implements PlayerService{
   
    private ModelMapper modelMapper;
    private PlayerRepository playerRepository;


    public PlayerServiceImpl( PlayerRepository playerRepository,
                ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.playerRepository = playerRepository;
    }
    
    // Mappers

    @Override
    public Player convertFromPlayerDto(PlayerDto playerDto) {
        return modelMapper.map(playerDto, Player.class);
    }

    @Override
    public PlayerDto convertToPlayerDto(Player player) {
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
    public List<Player> getAllPlayers() {
        return StreamSupport
            .stream(
                playerRepository.findAll().spliterator(),
                false)
        .collect(Collectors.toList());
    }
    
    @Override
    public Page<Player> getAllPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Override
    public Optional<Player> getPlayer(Long playerId) {
        Optional<Player> foundPlayer = playerRepository.findById(playerId);
        return foundPlayer;
    }

    @Override
    public void deletePlayer(Long playerId) {
        playerRepository.deleteById(playerId);
    }
    
}
