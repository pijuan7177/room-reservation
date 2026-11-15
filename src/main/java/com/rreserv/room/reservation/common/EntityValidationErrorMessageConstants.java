package com.rreserv.room.reservation.common;

public final class EntityValidationErrorMessageConstants {

    // Room

    public static final String ROOM_NAME_MUST_NOT_BE_EMPTY = "The room name must not be empty.";

    public static final String ROOM_NAME_TOO_LONG = "The room name is too long";

    public static final String ROOM_DESCRIPTION_MUST_NOT_BE_EMPTY = "The room description must not be empty.";

    public static final String ROOM_DESCRIPTION_TOO_LONG = "The room description is too long";

    public static final String ROOM_CAPACITY_IS_REQUIRED = "The room capacity is required.";

    public static final String ROOM_CAPACITY_MUST_BE_POSITIVE_VALUE = "The room capacity must be a positive value.";

    public static final String ROOM_SIZE_IS_REQUIRED = "The room size is required.";

    public static final String ROOM_SIZE_MUST_BE_POSITIVE_VALUE = "The room size must be a positive value.";

    // Reservation

    public static final String CUSTOMER_NAME_MUST_NOT_BE_EMPTY = "The customer name must not be empty.";

    public static final String CUSTOMER_NAME_TOO_LONG = "The customer name is too long";

    public static final String RESERVATION_START_DATE_IS_REQUIRED = "The reservation start date is required.";

    public static final String INVALID_RESERVATION_START_DATE = "The reservation start date is invalid";

    public static final String RESERVATION_END_DATE_IS_REQUIRED = "The reservation end date is required.";

    public static final String INVALID_RESERVATION_END_DATE = "The reservation end date is invalid";

    public static final String ROOM_IS_REQUIRED = "The room is required";

}
