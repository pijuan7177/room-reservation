package com.rreserv.room.reservation.rest.converter;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.rest.model.response.GetReservationPageResponse;
import com.rreserv.room.reservation.rest.model.response.GetReservationPageResponse.GetReservationResponse;
import com.rreserv.room.reservation.service.dto.GetReservationPageDto.GetReservationDto;
import com.rreserv.room.reservation.service.dto.GetReservationPageDto;

import lombok.RequiredArgsConstructor;

/**
 * Converts a GetRoomListDto to a GetRoomListResponse.
 */
@Component
@RequiredArgsConstructor(onConstructor_ = { @Lazy })
public class GetReservationPageDtoToGetReservationPageResponseConverter
        implements Converter<GetReservationPageDto, GetReservationPageResponse> {

    private final ConversionService conversionService;

    @Override
    public GetReservationPageResponse convert(GetReservationPageDto getReservationPageDto) {
        return Optional.ofNullable(getReservationPageDto)
                .map(dto -> GetReservationPageResponse.builder()
                        .total(dto.total())
                        .results(toGetReservationResponseList(dto.results()))
                        .build())
                .orElse(GetReservationPageResponse.builder().build());
    }

    private List<GetReservationResponse> toGetReservationResponseList(List<GetReservationDto> getReservationDtoList) {
        return getReservationDtoList.stream()
                .map(getResevationDto -> conversionService.convert(getResevationDto, GetReservationResponse.class))
                .toList();
    }

}
