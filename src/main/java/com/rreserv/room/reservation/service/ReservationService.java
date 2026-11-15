package com.rreserv.room.reservation.service;

import java.util.UUID;

import com.rreserv.room.reservation.service.dto.CreateReservationDto;
import com.rreserv.room.reservation.service.dto.GetReservationFilters;
import com.rreserv.room.reservation.service.dto.GetReservationPageDto;
import com.rreserv.room.reservation.service.dto.GetReservationPagination;

/**
 * Service interface for managing reservations.
 */
public interface ReservationService {

    UUID create(CreateReservationDto createReservationDto);

    GetReservationPageDto getPage(GetReservationFilters getReservationFilters,
            GetReservationPagination getReservationPagination);

    void cancel(UUID reservationId);

}
