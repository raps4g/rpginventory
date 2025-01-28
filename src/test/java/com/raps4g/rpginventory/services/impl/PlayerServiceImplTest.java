package com.raps4g.rpginventory.services.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.util.TestServiceData;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private PlayerRepository playerRepository;

    private Player inputPlayer;
    private Player savedPlayer;

    @BeforeEach
    public void setUp() {

        inputPlayer = TestServiceData.getUnsavedPlayer();
        savedPlayer = TestServiceData.getPlayer();
    }


    @Test
    void PlayerService_savePlayer_Saves() {

        Mockito.when(playerRepository.existsByName(inputPlayer.getName())).thenReturn(false);
        Mockito.when(playerRepository.save(inputPlayer)).thenReturn(savedPlayer);

        Player result = playerService.savePlayer(inputPlayer);

        Assertions.assertThat(result.getId()).isNotNull();
    }

    
    @Test
    void PlayerService_savePlayer_ThrowsException() {

        Mockito.when(playerRepository.existsByName(inputPlayer.getName())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> playerService.savePlayer(inputPlayer))
            .isInstanceOf(ResourceAlreadyExistsException.class);
    }
    
    @Test
    void PlayerService_savePlayer_Updates() {

        Mockito.when(playerRepository.existsByName(inputPlayer.getName())).thenReturn(true);
        Mockito.when(playerRepository.save(savedPlayer)).thenReturn(savedPlayer);

        Player result = playerService.savePlayer(savedPlayer);

        Assertions.assertThat(result.getId()).isNotNull();
    }
}
