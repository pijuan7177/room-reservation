package com.rreserv.room.reservation.rest.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.rest.model.request.CreateRoomRequest;
import com.rreserv.room.reservation.service.dto.CreateRoomDto;

/**
 * Converts a CreateRoomRequest to a CreateRoomDto.
 */
@Component
public class CreateRoomRequestToCreateRoomDtoConverter implements Converter<CreateRoomRequest, CreateRoomDto> {

    @Override
    public CreateRoomDto convert(CreateRoomRequest createRoomRequest) {
        return Optional.ofNullable(createRoomRequest)
                .map(request -> CreateRoomDto.builder()
                        .name(createRoomRequest.name())
                        .description(createRoomRequest.description())
                        .capacity(createRoomRequest.capacity())
                        .size(createRoomRequest.size())
                        .build())
                .orElse(CreateRoomDto.builder()
                    .build());
    }

}
