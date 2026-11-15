package com.rreserv.room.reservation.service.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.repository.model.ReservationPage.Reservation;
import com.rreserv.room.reservation.service.dto.GetReservationPageDto.GetReservationDto;

/**
 * Converts a Reservation to a GetReservationDto.
 */
@Component
public class ReservationToGetReservationDtoConverter implements Converter<Reservation, GetReservationDto> {

    @Override
    public GetReservationDto convert(Reservation reservation) {
        return Optional.ofNullable(reservation)
                .map(model -> GetReservationDto.builder()
                        .id(model.id())
                        .roomName(model.roomName())
                        .customerName(model.customerName())
                        .startDate(model.startDate())
                        .endDate(model.endDate())
                        .canceled(model.canceled())
                        .build())
                .orElse(GetReservationDto.builder().build());
    }

}
