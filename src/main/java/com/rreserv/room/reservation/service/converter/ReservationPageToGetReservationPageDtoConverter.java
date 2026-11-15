package com.rreserv.room.reservation.service.converter;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.repository.model.ReservationPage;
import com.rreserv.room.reservation.service.dto.GetReservationPageDto;

import lombok.RequiredArgsConstructor;

/**
 * Converts a ReservationPage to a GetReservationPageDto.
 */
@Component
@RequiredArgsConstructor(onConstructor_ = { @Lazy })
public class ReservationPageToGetReservationPageDtoConverter
        implements Converter<ReservationPage, GetReservationPageDto> {

    private final ConversionService conversionService;

    @Override
    public GetReservationPageDto convert(ReservationPage reservationPage) {
        return Optional.ofNullable(reservationPage)
                .map(model -> GetReservationPageDto.builder()
                        .total(model.total())
                        .results(toGetReservationDtoList(model.results()))
                        .build())
                .orElse(GetReservationPageDto.builder().build());
    }

    private List<GetReservationPageDto.GetReservationDto> toGetReservationDtoList(
            List<ReservationPage.Reservation> reservationList) {
        return reservationList.stream()
                .map(reservation -> conversionService.convert(reservation,
                        GetReservationPageDto.GetReservationDto.class))
                .toList();
    }

}
