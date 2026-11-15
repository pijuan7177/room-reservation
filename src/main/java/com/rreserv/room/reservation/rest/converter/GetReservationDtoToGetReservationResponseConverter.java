package com.rreserv.room.reservation.rest.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.rest.model.response.GetReservationPageResponse.GetReservationResponse;
import com.rreserv.room.reservation.service.dto.GetReservationPageDto.GetReservationDto;

/**
 * Converts a GetReservationDto to a GetReservationResponse.
 */
@Component
public class GetReservationDtoToGetReservationResponseConverter
        implements Converter<GetReservationDto, GetReservationResponse> {

    @Override
    public GetReservationResponse convert(GetReservationDto getReservationDto) {
        return Optional.ofNullable(getReservationDto)
                .map(dto -> GetReservationResponse.builder()
                        .id(dto.id())
                        .roomName(dto.roomName())
                        .customerName(dto.customerName())
                        .startDate(dto.startDate())
                        .endDate(dto.endDate())
                        .canceled(dto.canceled())
                        .build())
                .orElse(GetReservationResponse.builder().build());
    }

}
