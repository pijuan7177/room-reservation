package com.rreserv.room.reservation.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import com.rreserv.room.reservation.common.EntityPropertyConstraintConstants;
import com.rreserv.room.reservation.common.EntityValidationErrorMessageConstants;

/**
 * Represents a request to create a new room.
 */
@Builder
public record CreateRoomRequest(

        @NotBlank(message = EntityValidationErrorMessageConstants.ROOM_NAME_MUST_NOT_BE_EMPTY) 
        @Size(max = EntityPropertyConstraintConstants.ROOM_NAME_MAX_LENGTH, message = EntityValidationErrorMessageConstants.ROOM_NAME_TOO_LONG) 
        String name,

        @NotBlank(message = EntityValidationErrorMessageConstants.ROOM_DESCRIPTION_MUST_NOT_BE_EMPTY) 
        @Size(max = EntityPropertyConstraintConstants.ROOM_DESCRIPTION_MAX_LENGTH, message = EntityValidationErrorMessageConstants.ROOM_DESCRIPTION_TOO_LONG) 
        String description,

        @NotNull(message = EntityValidationErrorMessageConstants.ROOM_CAPACITY_IS_REQUIRED) 
        @Positive(message = EntityValidationErrorMessageConstants.ROOM_CAPACITY_MUST_BE_POSITIVE_VALUE)
        Integer capacity,

        @NotNull(message = EntityValidationErrorMessageConstants.ROOM_SIZE_IS_REQUIRED)
        @Positive(message = EntityValidationErrorMessageConstants.ROOM_SIZE_MUST_BE_POSITIVE_VALUE)
        Float size
) {
}
