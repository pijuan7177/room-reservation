package com.rreserv.room.reservation.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rreserv.room.reservation.model.Reservation;
import com.rreserv.room.reservation.model.Room;
import com.rreserv.room.reservation.repository.ReservationRepository;
import com.rreserv.room.reservation.repository.RoomRepository;
import com.rreserv.room.reservation.repository.model.ReservationPageCriteria;
import com.rreserv.room.reservation.repository.model.ReservationPagination;
import com.rreserv.room.reservation.service.ReservationService;
import com.rreserv.room.reservation.service.dto.CreateReservationDto;
import com.rreserv.room.reservation.service.dto.GetReservationFilters;
import com.rreserv.room.reservation.service.dto.GetReservationPageDto;
import com.rreserv.room.reservation.service.dto.GetReservationPagination;
import com.rreserv.room.reservation.service.exception.ReservationNotFoundException;
import com.rreserv.room.reservation.service.exception.RoomIsReservedException;
import com.rreserv.room.reservation.service.exception.RoomNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing reservations.
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ConversionService conversionService;

    private final ReservationRepository reservationRepository;

    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public UUID create(CreateReservationDto createReservationDto) {
        Room room = getRoomIfExists(createReservationDto.roomId());

        assertRoomIsNotReserved(createReservationDto.roomId(), createReservationDto.startDate(),
                createReservationDto.endDate());

        Reservation reservation = conversionService.convert(createReservationDto, Reservation.class);
        reservation.setId(UUID.randomUUID());
        reservation.setCanceled(Boolean.FALSE);
        reservation.setRoom(room);

        reservationRepository.save(reservation);

        return reservation.getId();
    }

    @Override 
    public GetReservationPageDto getPage(GetReservationFilters getReservationFilters,
            GetReservationPagination getReservationPagination) {
        return conversionService.convert(reservationRepository.findPageByCriteria(
                conversionService.convert(getReservationFilters, ReservationPageCriteria.class),
                conversionService.convert(getReservationPagination, ReservationPagination.class)),
                GetReservationPageDto.class);
    }

    @Override
    @Transactional
    public void cancel(UUID reservationId) {
        Reservation reservation = getReservationIfExists(reservationId);
        reservation.setCanceled(Boolean.TRUE);

        reservationRepository.save(reservation);
    }

    private Room getRoomIfExists(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException());
    }

    private Reservation getReservationIfExists(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException());
    }

    private void assertRoomIsNotReserved(UUID roomId, Instant startDate, Instant endDate) {
        if (reservationRepository.countNotCanceledByRoomIdAndDateRange(roomId, startDate, endDate) > 0) {
            throw new RoomIsReservedException();
        }
    }

}
