package com.rreserv.room.reservation.service;

import java.util.UUID;

import com.rreserv.room.reservation.service.dto.CreateRoomDto;
import com.rreserv.room.reservation.service.dto.GetRoomListDto;

/**
 * Service interface for managing rooms.
 */
public interface RoomService {

    UUID create(CreateRoomDto createRoomDto);

    GetRoomListDto getList();

}
