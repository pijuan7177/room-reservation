package com.rreserv.room.reservation.service.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.model.Room;
import com.rreserv.room.reservation.service.dto.GetRoomListDto.GetRoomDto;

/**
 * Converts a Room to a GetRoomDto.
 */
@Component
public class RoomToGetRoomDtoConverter implements Converter<Room, GetRoomDto> {

    @Override
    public GetRoomDto convert(Room room) {
        return Optional.ofNullable(room)
                .map(entity -> GetRoomDto.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .description(entity.getDescription())
                        .capacity(entity.getCapacity())
                        .size(entity.getSize())
                        .build())
                .orElse(GetRoomDto.builder().build());
    }

}
