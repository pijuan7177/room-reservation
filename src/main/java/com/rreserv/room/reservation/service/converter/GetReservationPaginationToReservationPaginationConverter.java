package com.rreserv.room.reservation.service.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.repository.model.ReservationPagination;
import com.rreserv.room.reservation.repository.model.ReservationSortBy;
import com.rreserv.room.reservation.repository.model.SortDirection;
import com.rreserv.room.reservation.service.dto.GetReservationPagination;

/**
 * Converts a GetReservationPagination to a ReservationPagination.
 */
@Component
public class GetReservationPaginationToReservationPaginationConverter
        implements Converter<GetReservationPagination, ReservationPagination> {

    @Override
    public ReservationPagination convert(GetReservationPagination getReservationPagination) {
        return Optional.ofNullable(getReservationPagination)
                .map(pagination -> ReservationPagination.builder()
                        .offset(pagination.offset())
                        .limit(pagination.limit())
                        .sortBy(ReservationSortBy.valueOf(pagination.sortBy().name()))
                        .sortDir(SortDirection.valueOf(pagination.sortDir().name()))
                        .build())
                .orElse(ReservationPagination.builder().build());
    }

}
