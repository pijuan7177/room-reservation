package com.rreserv.room.reservation.model;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.rreserv.room.reservation.common.EntityPropertyConstraintConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Models a room.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROOMS")
public class Room {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "ID")
    private UUID id;

    @NotBlank
    @Column(name = "ROOM_NAME", nullable = false, length = EntityPropertyConstraintConstants.ROOM_NAME_MAX_LENGTH, unique = true)
    private String name;

    @NotBlank
    @Column(name = "DESCRIPTION", length = EntityPropertyConstraintConstants.ROOM_DESCRIPTION_MAX_LENGTH, nullable = false)
    private String description;

    @NotNull
    @Column(name = "CAPACITY", nullable = false)
    private Integer capacity;

    @NotNull
    @Column(name = "SIZE", nullable = false)
    private Float size;

}
