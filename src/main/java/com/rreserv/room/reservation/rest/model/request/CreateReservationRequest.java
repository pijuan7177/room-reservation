package com.rreserv.room.reservation.rest.model.request;

import java.time.Instant;
import java.util.UUID;

import com.rreserv.room.reservation.common.EntityPropertyConstraintConstants;
import com.rreserv.room.reservation.common.EntityValidationErrorMessageConstants;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Represents a request to create a new reservation.
 */
@Builder
public record CreateReservationRequest(

        @NotBlank(message = EntityValidationErrorMessageConstants.CUSTOMER_NAME_TOO_LONG) 
        @Size(max = EntityPropertyConstraintConstants.CUSTOMER_NAME_MAX_LENGTH, message = EntityValidationErrorMessageConstants.CUSTOMER_NAME_TOO_LONG)
        String customerName,

        @NotNull(message = EntityValidationErrorMessageConstants.RESERVATION_START_DATE_IS_REQUIRED) 
        @Future(message = EntityValidationErrorMessageConstants.INVALID_RESERVATION_START_DATE) 
        Instant startDate,

        @NotNull(message = EntityValidationErrorMessageConstants.RESERVATION_END_DATE_IS_REQUIRED) 
        @Future(message = EntityValidationErrorMessageConstants.INVALID_RESERVATION_END_DATE)
        Instant endDate,

        @NotNull(message = EntityValidationErrorMessageConstants.ROOM_IS_REQUIRED) 
        UUID roomId
) {
}
