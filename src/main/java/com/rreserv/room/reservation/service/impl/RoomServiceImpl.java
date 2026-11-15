package com.rreserv.room.reservation.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rreserv.room.reservation.model.Room;
import com.rreserv.room.reservation.repository.RoomRepository;
import com.rreserv.room.reservation.service.RoomService;
import com.rreserv.room.reservation.service.dto.CreateRoomDto;
import com.rreserv.room.reservation.service.dto.GetRoomListDto;
import com.rreserv.room.reservation.service.dto.GetRoomListDto.GetRoomDto;
import com.rreserv.room.reservation.service.exception.RoomAlreadyExistsException;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing rooms.
 */
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final ConversionService conversionService;

    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public UUID create(CreateRoomDto createRoomDto) {
        assertRoomNotExists(createRoomDto.name());

        Room room = conversionService.convert(createRoomDto, Room.class);
        room.setId(UUID.randomUUID());
        roomRepository.save(room);
        return room.getId();
    }

    @Override
    public GetRoomListDto getList() {
        return GetRoomListDto.builder()
                .total(roomRepository.count())
                .results(toGetRoomDtoList(roomRepository.findAll()))
                .build();
    }

    private void assertRoomNotExists(String roomName) {
        if (roomRepository.existsByName(roomName)) {
            throw new RoomAlreadyExistsException();
        }
    }

    private List<GetRoomListDto.GetRoomDto> toGetRoomDtoList(List<Room> roomList) {
        return roomList.stream()
                .map(room -> conversionService.convert(room, GetRoomDto.class))
                .toList();
    }

}
