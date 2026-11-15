package com.rreserv.room.reservation.rest.converter;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.rest.model.response.GetRoomListResponse;
import com.rreserv.room.reservation.service.dto.GetRoomListDto;

import lombok.RequiredArgsConstructor;

/**
 * Converts a GetRoomListDto to a GetRoomListResponse.
 */
@Component
@RequiredArgsConstructor(onConstructor_ = { @Lazy })
public class GetRoomListDtoToGetRoomListResponseConverter implements Converter<GetRoomListDto, GetRoomListResponse> {

    private final ConversionService conversionService;

    @Override
    public GetRoomListResponse convert(GetRoomListDto getRoomListDto) {
        return Optional.ofNullable(getRoomListDto)
                .map(dto -> GetRoomListResponse.builder()
                        .total(dto.total())
                        .results(toGetRoomResponseList(dto.results()))
                        .build())
                .orElse(GetRoomListResponse.builder().build());
    }

    private List<GetRoomListResponse.GetRoomResponse> toGetRoomResponseList(List<GetRoomListDto.GetRoomDto> getRoomDtoList) {
        return getRoomDtoList.stream()
                .map(getRoomDto -> conversionService.convert(getRoomDto, GetRoomListResponse.GetRoomResponse.class))
                .toList();
    }

}
