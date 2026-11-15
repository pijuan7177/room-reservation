package com.rreserv.room.reservation.rest;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rreserv.room.reservation.common.EntityPropertyConstraintConstants;
import com.rreserv.room.reservation.rest.model.request.CreateReservationRequest;
import com.rreserv.room.reservation.rest.model.response.CreateReservationResponse;
import com.rreserv.room.reservation.rest.model.response.GetReservationPageResponse;
import com.rreserv.room.reservation.service.ReservationService;
import com.rreserv.room.reservation.service.dto.CreateReservationDto;
import com.rreserv.room.reservation.service.dto.GetReservationFilters;
import com.rreserv.room.reservation.service.dto.GetReservationPagination;
import com.rreserv.room.reservation.service.dto.GetReservationSortBy;
import com.rreserv.room.reservation.service.dto.SortDirection;
import com.rreserv.room.reservation.service.exception.ReservationNotFoundException;
import com.rreserv.room.reservation.service.exception.RoomIsReservedException;
import com.rreserv.room.reservation.service.exception.RoomNotFoundException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing reservations
 */
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

        private static final String CREATE_RESERVATION_RESPONSE_LOCATION_URI_PATTERN = "/reservations/%s";

        private final ConversionService conversionService;

        private final ReservationService reservationService;

        @PostMapping
        @SecurityRequirement(name = "bearerAuth")
        public ResponseEntity<CreateReservationResponse> create(
                        @Valid @RequestBody CreateReservationRequest createReservationRequest) {
                try {
                        UUID createdReservationId = reservationService.create(
                                        conversionService.convert(createReservationRequest,
                                                        CreateReservationDto.class));

                        CreateReservationResponse createReservationResponse = CreateReservationResponse.builder()
                                        .id(createdReservationId)
                                        .build();

                        return ResponseEntity.created(
                                        URI.create(CREATE_RESERVATION_RESPONSE_LOCATION_URI_PATTERN
                                                        .formatted(createdReservationId.toString())))
                                        .body(createReservationResponse);
                } catch (RoomNotFoundException roomNotFoundException) {
                        return ResponseEntity.notFound().build();
                } catch (RoomIsReservedException roomIsReservedException) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
        }

        @GetMapping
        public ResponseEntity<GetReservationPageResponse> getPage(
                        @RequestParam(name = "room_name", required = false) 
                        String roomName,
                        @RequestParam(name = "start_date", required = false) 
                        Instant startDate,
                        @RequestParam(name = "end_date", required = false) 
                        Instant endDate,
                        @RequestParam(name = "offset", required = false, defaultValue = "0") 
                        @PositiveOrZero 
                        int offset,
                        @RequestParam(name = "limit", required = false, defaultValue = "10") 
                        @Min(EntityPropertyConstraintConstants.LIMIT_MIN_VALUE) 
                        @Max(EntityPropertyConstraintConstants.LIMIT_MAX_VALUE) 
                        int limit,
                        @RequestParam(name = "sort_by", required = false, defaultValue = "START_DATE") 
                        String sortBy,
                        @RequestParam(name = "sort_dir", required = false, defaultValue = "DESC") 
                        String sortDir) {
                GetReservationFilters getReservationFilters = GetReservationFilters.builder()
                                .roomName(roomName)
                                .startDate(startDate)
                                .endDate(endDate)
                                .build();

                GetReservationPagination getReservationPagination = GetReservationPagination.builder()
                                .offset(offset)
                                .limit(limit)
                                .sortBy(GetReservationSortBy.valueOf(sortBy))
                                .sortDir(SortDirection.valueOf(sortDir))
                                .build();

                return ResponseEntity.ok(conversionService.convert(
                                reservationService.getPage(getReservationFilters, getReservationPagination),
                                GetReservationPageResponse.class));
        }

        @PatchMapping("/{reservationId}/cancel")
        @SecurityRequirement(name = "bearerAuth")
        public ResponseEntity<Void> cancel(
                        @PathVariable UUID reservationId) {
                try {
                        reservationService.cancel(reservationId);
                        return ResponseEntity.noContent().build();
                } catch (ReservationNotFoundException reservationNotFoundException) {
                        return ResponseEntity.notFound().build();
                }
        }

}
