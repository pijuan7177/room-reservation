package com.rreserv.room.reservation.common;

/**
 * Constraints applied to entities and dto definitions.
 */
public final class EntityPropertyConstraintConstants {

    // Room

    public static final int ROOM_NAME_MAX_LENGTH = 30;
    public static final int ROOM_DESCRIPTION_MAX_LENGTH = 500;

    // Reservation

    public static final int CUSTOMER_NAME_MAX_LENGTH = 50;

    // Default pagination limits for GET endpoints

    public static final int LIMIT_MIN_VALUE = 10;
    public static final int LIMIT_MAX_VALUE = 100;

}
