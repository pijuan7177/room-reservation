package com.rreserv.room.reservation.rest.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.rest.model.response.GetRoomListResponse.GetRoomResponse;
import com.rreserv.room.reservation.service.dto.GetRoomListDto.GetRoomDto;

/**
 * Converts a GetRoomDto to a GetRoomResponse.
 */
@Component
public class GetRoomDtoToGetRoomResponseConverter implements Converter<GetRoomDto, GetRoomResponse> {

    @Override
    public GetRoomResponse convert(GetRoomDto getRoomDto) {
        return Optional.ofNullable(getRoomDto)
                .map(dto -> GetRoomResponse.builder()
                        .id(dto.id())
                        .name(dto.name())
                        .description(dto.description())
                        .capacity(dto.capacity())
                        .size(dto.size())
                        .build())
                .orElse(GetRoomResponse.builder().build());
    }

}
