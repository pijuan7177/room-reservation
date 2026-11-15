package com.rreserv.room.reservation.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.rreserv.room.reservation.common.EntityPropertyConstraintConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Models a reservation.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RESERVATIONS")
public class Reservation {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "ID")
    private UUID id;

    @NotBlank
    @Column(name = "CUSTOMER_NAME", nullable = false, length = EntityPropertyConstraintConstants.CUSTOMER_NAME_MAX_LENGTH)
    private String customerName;

    @NotNull
    @Column(name = "START_DATE", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "END_DATE", nullable = false)
    private Instant endDate;

    @Column(name = "CANCELED")
    private Boolean canceled;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROOM_ID", nullable = false)
    private Room room;

}
