package com.rreserv.room.reservation.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import com.rreserv.room.reservation.model.Room;
import com.rreserv.room.reservation.repository.RoomRepository;
import com.rreserv.room.reservation.service.dto.CreateRoomDto;
import com.rreserv.room.reservation.service.exception.RoomAlreadyExistsException;

/**
 * Unit tests for {@link RoomServiceImpl#create(CreateRoomDto)}.
 */
@ExtendWith(MockitoExtension.class)
class RoomServiceImplTests {

    @Mock
    private ConversionService conversionService;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void createSavesRoomWithGeneratedIdAndReturnsIt() {
        CreateRoomDto createRoomDto = createRoomDto();
        Room room = roomFrom(createRoomDto);

        when(roomRepository.existsByName(createRoomDto.name())).thenReturn(false);
        when(conversionService.convert(createRoomDto, Room.class)).thenReturn(room);

        UUID createdRoomId = roomService.create(createRoomDto);

        ArgumentCaptor<Room> roomCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).existsByName(createRoomDto.name());
        verify(conversionService).convert(createRoomDto, Room.class);
        verify(roomRepository).save(roomCaptor.capture());

        Room savedRoom = roomCaptor.getValue();

        assertThat(createdRoomId).isNotNull();
        assertThat(savedRoom.getId()).isEqualTo(createdRoomId);
        assertThat(savedRoom.getName()).isEqualTo(createRoomDto.name());
        assertThat(savedRoom.getDescription()).isEqualTo(createRoomDto.description());
        assertThat(savedRoom.getCapacity()).isEqualTo(createRoomDto.capacity());
        assertThat(savedRoom.getSize()).isEqualTo(createRoomDto.size());
    }

    @Test
    void createDoesNotPersistRoomWhenNameAlreadyExists() {
        CreateRoomDto createRoomDto = createRoomDto();

        when(roomRepository.existsByName(createRoomDto.name())).thenReturn(true);

        assertThatThrownBy(() -> roomService.create(createRoomDto))
                .isInstanceOf(RoomAlreadyExistsException.class);

        verify(roomRepository).existsByName(createRoomDto.name());
        verify(roomRepository, never()).save(any(Room.class));
        verifyNoInteractions(conversionService);
    }

    private CreateRoomDto createRoomDto() {
        return CreateRoomDto.builder()
                .name("Room A")
                .description("Room used by the unit tests")
                .capacity(10)
                .size(25.0f)
                .build();
    }

    private Room roomFrom(CreateRoomDto createRoomDto) {
        return Room.builder()
                .name(createRoomDto.name())
                .description(createRoomDto.description())
                .capacity(createRoomDto.capacity())
                .size(createRoomDto.size())
                .build();
    }
}
