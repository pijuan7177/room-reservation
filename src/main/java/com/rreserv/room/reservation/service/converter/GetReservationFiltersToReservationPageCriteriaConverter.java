package com.rreserv.room.reservation.service.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.repository.model.ReservationPageCriteria;
import com.rreserv.room.reservation.service.dto.GetReservationFilters;

/**
 * Converts a GetReservationFilters to a ReservationPageCriteria.
 */
@Component
public class GetReservationFiltersToReservationPageCriteriaConverter
        implements Converter<GetReservationFilters, ReservationPageCriteria> {

    @Override
    public ReservationPageCriteria convert(GetReservationFilters getReservationFilters) {
        return Optional.ofNullable(getReservationFilters)
                .map(filters -> ReservationPageCriteria.builder()
                        .roomName(filters.roomName())
                        .startDate(filters.startDate())
                        .endDate(filters.endDate())
                        .build())
                .orElse(ReservationPageCriteria.builder().build());
    }

}
