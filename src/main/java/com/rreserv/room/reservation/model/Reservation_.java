package com.rreserv.room.reservation.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Reservation.class)
public abstract class Reservation_ {

    public static volatile SingularAttribute<Reservation, UUID> id;
    public static volatile SingularAttribute<Reservation, String> customerName;
    public static volatile SingularAttribute<Reservation, Instant> startDate;
    public static volatile SingularAttribute<Reservation, Instant> endDate;
    public static volatile SingularAttribute<Reservation, Boolean> canceled;
    public static volatile SingularAttribute<Reservation, Room> room;

}
