package com.rreserv.room.reservation.rest;

import java.net.URI;
import java.util.UUID;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rreserv.room.reservation.rest.model.request.CreateRoomRequest;
import com.rreserv.room.reservation.rest.model.response.CreateRoomResponse;
import com.rreserv.room.reservation.rest.model.response.GetRoomListResponse;
import com.rreserv.room.reservation.service.RoomService;
import com.rreserv.room.reservation.service.dto.CreateRoomDto;
import com.rreserv.room.reservation.service.exception.RoomAlreadyExistsException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing rooms
 */
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

        private static final String CREATE_ROOM_RESPONSE_LOCATION_URI_PATTERN = "/rooms/%s";

        private final ConversionService conversionService;

        private final RoomService roomService;

        @PostMapping
        @SecurityRequirement(name = "bearerAuth")
        public ResponseEntity<CreateRoomResponse> create(
                        @Valid @RequestBody CreateRoomRequest createRoomRequest) {
                try {
                        UUID createdRoomId = roomService.create(
                                        conversionService.convert(createRoomRequest, CreateRoomDto.class));

                        CreateRoomResponse createRoomResponse = CreateRoomResponse.builder()
                                        .id(createdRoomId)
                                        .build();

                        return ResponseEntity.created(
                                        URI.create(CREATE_ROOM_RESPONSE_LOCATION_URI_PATTERN
                                                        .formatted(createdRoomId.toString())))
                                        .body(createRoomResponse);
                } catch (RoomAlreadyExistsException roomAlreadyExistsException) {
                        return ResponseEntity.status(HttpStatus.CONFLICT.value()).build();
                }
        }

        @GetMapping
        public ResponseEntity<GetRoomListResponse> getList() {
                return ResponseEntity.ok(
                                conversionService.convert(roomService.getList(), GetRoomListResponse.class));
        }

}
