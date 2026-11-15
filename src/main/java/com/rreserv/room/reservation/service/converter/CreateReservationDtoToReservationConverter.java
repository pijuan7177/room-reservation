package com.rreserv.room.reservation.service.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.model.Reservation;
import com.rreserv.room.reservation.service.dto.CreateReservationDto;

/**
 * Converts a CreateReservationDto to a Reservation.
 */
@Component
public class CreateReservationDtoToReservationConverter implements Converter<CreateReservationDto, Reservation> {

    @Override
    public Reservation convert(CreateReservationDto createReservationDto) {
        return Optional.ofNullable(createReservationDto)
                .map(dto -> Reservation.builder()
                        .customerName(dto.customerName())
                        .startDate(dto.startDate())
                        .endDate(dto.endDate())
                        .build())
                .orElse(Reservation.builder()
                    .build());
    }

}
