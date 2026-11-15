package com.rreserv.room.reservation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rreserv.room.reservation.model.Room;

/**
 * Repository for managing Room entities.
 */
public interface RoomRepository extends JpaRepository<Room, UUID> {

    boolean existsByName(String name);    

}
