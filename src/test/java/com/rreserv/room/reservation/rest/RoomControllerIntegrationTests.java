package com.rreserv.room.reservation.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;
import com.rreserv.room.reservation.model.Room;
import com.rreserv.room.reservation.repository.RoomRepository;
import com.rreserv.room.reservation.rest.model.request.CreateRoomRequest;
import com.rreserv.room.reservation.rest.model.request.LoginRequest;

import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for {@link RoomController#create(CreateRoomRequest)}.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RoomControllerIntegrationTests {

    private static final String ROOM_DESCRIPTION = "Room created by the integration tests";

    private static final int ROOM_CAPACITY = 10;

    private static final float ROOM_SIZE = 30.5f;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Value("${app.security.username}")
    private String username;

    @Value("${app.security.password}")
    private String password;

    private String authorizationHeader;

    @BeforeEach
    void logIn() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequest(username, password))))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = JsonPath.read(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), "$.accessToken");

        authorizationHeader = "Bearer " + accessToken;
    }

    @Test
    void createReturnsUnauthorizedWhenTokenIsMissing() throws Exception {
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRoomJson(uniqueRoomName())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createReturnsUnauthorizedWhenTokenIsInvalid() throws Exception {
        mockMvc.perform(post("/rooms")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRoomJson(uniqueRoomName())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createReturnsCreatedAndPersistsRoomWhenAuthenticated() throws Exception {
        String roomName = uniqueRoomName();

        MvcResult result = mockMvc.perform(post("/rooms")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRoomJson(roomName)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String roomId = JsonPath.read(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), "$.id");

        assertThat(result.getResponse().getHeader(HttpHeaders.LOCATION))
                .isEqualTo("/rooms/" + roomId);

        Room room = roomRepository.findById(UUID.fromString(roomId)).orElseThrow();
        assertThat(room.getName()).isEqualTo(roomName);
        assertThat(room.getDescription()).isEqualTo(ROOM_DESCRIPTION);
        assertThat(room.getCapacity()).isEqualTo(ROOM_CAPACITY);
        assertThat(room.getSize()).isEqualTo(ROOM_SIZE);
    }

    @Test
    void createReturnsBadRequestWhenRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/rooms")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateRoomRequest("", "", 0, -1.0f))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReturnsConflictWhenRoomNameAlreadyExists() throws Exception {
        String roomName = uniqueRoomName();

        mockMvc.perform(post("/rooms")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRoomJson(roomName)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/rooms")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRoomJson(roomName)))
                .andExpect(status().isConflict());
    }

    private String uniqueRoomName() {
        return "it-room-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String createRoomJson(String roomName) throws Exception {
        return objectMapper.writeValueAsString(
                new CreateRoomRequest(roomName, ROOM_DESCRIPTION, ROOM_CAPACITY, ROOM_SIZE));
    }
}
