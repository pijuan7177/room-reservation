package com.rreserv.room.reservation.repository;

import com.rreserv.room.reservation.repository.model.ReservationPage;
import com.rreserv.room.reservation.repository.model.ReservationPageCriteria;
import com.rreserv.room.reservation.repository.model.ReservationPagination;

/**
 * ReservationCriteriaRepository
 */
public interface ReservationCriteriaRepository {

    ReservationPage findPageByCriteria(ReservationPageCriteria reservationPageCriteria,
            ReservationPagination reservationPagination);

}
