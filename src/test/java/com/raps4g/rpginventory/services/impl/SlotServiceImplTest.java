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
import com.raps4g.rpginventory.model.Slot;
import com.raps4g.rpginventory.repositories.SlotRepository;
import com.raps4g.rpginventory.util.TestServiceData;

@ExtendWith(MockitoExtension.class)
public class SlotServiceImplTest {
 
    @InjectMocks
    private SlotServiceImpl slotService;

    @Mock 
    private SlotRepository slotRepository;

    private Slot inputSlot;
    private Slot savedSlot;

    @BeforeEach
    public void setUp() {

        inputSlot = TestServiceData.getUnsavedRightHandSlot();
        savedSlot = TestServiceData.getRightHandSlot();
    }

    @Test
    public void SlotServcie_Save_Saves() {
        Mockito.when(slotRepository.existsByName(inputSlot.getName())).thenReturn(false);
        Mockito.when(slotRepository.save(inputSlot)).thenReturn(savedSlot);

        Slot result = slotService.saveSlot(inputSlot);

        Assertions.assertThat(result).isEqualTo(savedSlot);

    }

    @Test
    public void SlotServcie_Save_ThrowsException() {

        Mockito.when(slotRepository.existsByName(inputSlot.getName())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> slotService.saveSlot(inputSlot))
            .isInstanceOf(ResourceAlreadyExistsException.class);
    }

}
