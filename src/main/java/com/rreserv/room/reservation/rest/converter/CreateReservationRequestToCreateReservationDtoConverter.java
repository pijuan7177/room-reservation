package com.rreserv.room.reservation.rest.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.rest.model.request.CreateReservationRequest;
import com.rreserv.room.reservation.service.dto.CreateReservationDto;

/**
 * Converts a CreateReservationRequest to a CreateReservationDto.
 */
@Component
public class CreateReservationRequestToCreateReservationDtoConverter
        implements Converter<CreateReservationRequest, CreateReservationDto> {

    @Override
    public CreateReservationDto convert(CreateReservationRequest createReservationRequest) {
        return Optional.ofNullable(createReservationRequest)
                .map(request -> CreateReservationDto.builder()
                        .customerName(request.customerName())
                        .startDate(request.startDate())
                        .endDate(request.endDate())
                        .roomId(request.roomId())
                        .build())
                .orElse(CreateReservationDto.builder()
                        .build());
    }

}
