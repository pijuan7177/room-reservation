package com.rreserv.room.reservation.model;

import java.util.UUID;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Room.class)
public abstract class Room_ {

    public static volatile SingularAttribute<Room, UUID> id;
    public static volatile SingularAttribute<Room, String> name;
    public static volatile SingularAttribute<Room, String> description;
    public static volatile SingularAttribute<Room, Integer> capacity;
    public static volatile SingularAttribute<Room, Float> size;

}
