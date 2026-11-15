package com.rreserv.room.reservation.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.rreserv.room.reservation.model.Reservation;
import com.rreserv.room.reservation.model.Reservation_;
import com.rreserv.room.reservation.model.Room;
import com.rreserv.room.reservation.model.Room_;
import com.rreserv.room.reservation.repository.ReservationCriteriaRepository;
import com.rreserv.room.reservation.repository.model.ReservationPage;
import com.rreserv.room.reservation.repository.model.ReservationPageCriteria;
import com.rreserv.room.reservation.repository.model.ReservationPagination;
import com.rreserv.room.reservation.repository.model.SortDirection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationCriteriaRepositoryImpl implements ReservationCriteriaRepository {

    private final EntityManager entityManager;

    @Override 
    public ReservationPage findPageByCriteria(
            ReservationPageCriteria reservationPageCriteria,
            ReservationPagination reservationPagination) {
        Long total = executeCountQuery(reservationPageCriteria);

        List<ReservationPage.Reservation> results = executeSelectQuery(reservationPageCriteria, reservationPagination);

        return ReservationPage.builder()
                .total(total)
                .results(results)
                .build();
    }

    private Long executeCountQuery(ReservationPageCriteria reservationPageCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();

        Root<Reservation> reservationRoot = criteriaQuery.from(Reservation.class);

        Join<Reservation, Room> roomJoin = reservationRoot.join(Reservation_.room, JoinType.INNER);

        List<Predicate> predicates = buildPredicates(reservationPageCriteria, criteriaBuilder, reservationRoot,
                roomJoin);

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        criteriaQuery.select(criteriaBuilder.countDistinct(reservationRoot));

        return (Long) entityManager.createQuery(criteriaQuery)
                .getSingleResult();
    }

    private List<ReservationPage.Reservation> executeSelectQuery(
            ReservationPageCriteria reservationPageCriteria,
            ReservationPagination reservationPagination) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();

        Root<Reservation> reservationRoot = criteriaQuery.from(Reservation.class);

        Join<Reservation, Room> roomJoin = reservationRoot.join(Reservation_.room, JoinType.INNER);

        List<Predicate> predicates = buildPredicates(reservationPageCriteria, criteriaBuilder, reservationRoot,
                roomJoin);

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        criteriaQuery.select(
                criteriaBuilder.construct(
                        ReservationPage.Reservation.class,
                        reservationRoot.get(Reservation_.id),
                        roomJoin.get(Room_.name).alias("roomName"),
                        reservationRoot.get(Reservation_.customerName),
                        reservationRoot.get(Reservation_.startDate),
                        reservationRoot.get(Reservation_.endDate),
                        reservationRoot.get(Reservation_.canceled)));

        applySorting(reservationPagination, criteriaBuilder, criteriaQuery, reservationRoot, roomJoin);

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(reservationPagination.offset())
                .setMaxResults(reservationPagination.limit())
                .getResultList()
                .stream()
                .map(ReservationPage.Reservation.class::cast)
                .toList();
    }

    private List<Predicate> buildPredicates(
            ReservationPageCriteria reservationPageCriteria,
            CriteriaBuilder criteriaBuilder,
            Root<Reservation> reservationRoot,
            Join<Reservation, Room> roomJoin) {
        List<Predicate> predicates = buildReservationPredicates(reservationPageCriteria, criteriaBuilder,
                reservationRoot);

        predicates.addAll(buildRoomPredicates(reservationPageCriteria, criteriaBuilder, roomJoin));

        return predicates;
    }

    private List<Predicate> buildReservationPredicates(
            ReservationPageCriteria reservationPageCriteria,
            CriteriaBuilder criteriaBuilder,
            Root<Reservation> reservationRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(reservationPageCriteria.startDate())) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(reservationRoot.get(Reservation_.startDate),
                    reservationPageCriteria.startDate()));
        }

        if (Objects.nonNull(reservationPageCriteria.endDate())) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(reservationRoot.get(Reservation_.endDate),
                    reservationPageCriteria.endDate()));
        }

        return predicates;
    }

    private List<Predicate> buildRoomPredicates(
            ReservationPageCriteria reservationPageCriteria,
            CriteriaBuilder criteriaBuilder,
            Join<Reservation, Room> roomJoin) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNoneBlank(reservationPageCriteria.roomName())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(roomJoin.get(Room_.name)),
                    "%" + reservationPageCriteria.roomName().toLowerCase() + "%"));
        }

        return predicates;
    }

    private void applySorting(
            ReservationPagination reservationPagination,
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<Object> criteriaQuery,
            Root<Reservation> reservationRoot,
            Join<Reservation, Room> roomJoin) {
        Expression<?> primaryExpression = getOrderByExpression(reservationPagination, criteriaBuilder, criteriaQuery,
                reservationRoot, roomJoin);
        Expression<?> secondaryExpression = reservationRoot.get(Reservation_.startDate);

        if (SortDirection.ASC.equals(reservationPagination.sortDir())) {
            criteriaQuery.orderBy(
                    criteriaBuilder.asc(primaryExpression),
                    criteriaBuilder.asc(secondaryExpression));
        } else {
            criteriaQuery.orderBy(
                    criteriaBuilder.desc(primaryExpression),
                    criteriaBuilder.desc(secondaryExpression));
        }
    }

    private Expression<?> getOrderByExpression(
            ReservationPagination reservationPagination,
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<Object> criteriaQuery,
            Root<Reservation> reservationRoot,
            Join<Reservation, Room> roomJoin) {
        return switch (reservationPagination.sortBy()) {
            case ROOM_NAME -> roomJoin.get(Room_.name);
            case END_DATE -> reservationRoot.get(Reservation_.endDate);
            default -> reservationRoot.get(Reservation_.startDate);
        };
    }

}
