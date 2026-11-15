package com.rreserv.room.reservation.service.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.model.Room;
import com.rreserv.room.reservation.service.dto.CreateRoomDto;

/**
 * Converts a CreateRoomDto to a Room.
 */
@Component
public class CreateRoomDtoToRoomConverter implements Converter<CreateRoomDto, Room> {

    @Override
    public Room convert(CreateRoomDto createRoomDto) {
        return Optional.ofNullable(createRoomDto)
                .map(dto -> Room.builder()
                        .name(dto.name())
                        .description(dto.description())
                        .capacity(dto.capacity())
                        .size(dto.size())
                        .build())
                .orElse(Room.builder()
                        .build());
    }

}
