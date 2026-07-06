package com.cuscatlan.coworking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import com.cuscatlan.coworking.dto.request.space.CreateSpaceRequest;
import com.cuscatlan.coworking.dto.request.space.UpdateSpaceRequest;
import com.cuscatlan.coworking.dto.response.space.SpaceResponse;
import com.cuscatlan.coworking.enums.SpaceType;
import com.cuscatlan.coworking.service.SpaceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpaceService spaceService;

    @Test
    @DisplayName("Should create space successfully")
    void shouldCreateSpaceSuccessfully() throws Exception {

        CreateSpaceRequest request = new CreateSpaceRequest();

        request.setName("Sala IT");
        request.setType(SpaceType.MEETING_ROOM);
        request.setCapacity(10);
        request.setLocation("Nivel 5");
        request.setPricePerHour(BigDecimal.TEN);

        SpaceResponse response =
                new SpaceResponse(
                        1L,
                        "Sala IT",
                        SpaceType.MEETING_ROOM,
                        10,
                        "Nivel 5",
                        BigDecimal.TEN,
                        true);

        when(spaceService.create(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/spaces")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("Space created successfully."))

                .andExpect(jsonPath("$.data.id").value(1))

                .andExpect(jsonPath("$.data.name")
                        .value("Sala IT"));

    }

    @Test
    @DisplayName("Should update space successfully")
    void shouldUpdateSpaceSuccessfully() throws Exception {

        UpdateSpaceRequest request = new UpdateSpaceRequest();

        request.setName("Sala Gerencia");
        request.setType(SpaceType.MEETING_ROOM);
        request.setCapacity(15);
        request.setLocation("Nivel 6");
        request.setPricePerHour(BigDecimal.valueOf(15));
        request.setActive(true);

        SpaceResponse response =
                new SpaceResponse(
                        1L,
                        "Sala Gerencia",
                        SpaceType.MEETING_ROOM,
                        15,
                        "Nivel 6",
                        BigDecimal.valueOf(15),
                        true);

        when(spaceService.update(eq(1L), any()))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/v1/spaces/1")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("Space updated successfully."))

                .andExpect(jsonPath("$.data.name")
                        .value("Sala Gerencia"));

    }

    @Test
    @DisplayName("Should find space by id")
    void shouldFindSpaceById() throws Exception {

        SpaceResponse response =
                new SpaceResponse(
                        1L,
                        "Sala IT",
                        SpaceType.MEETING_ROOM,
                        10,
                        "Nivel 5",
                        BigDecimal.TEN,
                        true);

        when(spaceService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/spaces/1"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("Space retrieved successfully."))

                .andExpect(jsonPath("$.data.id")
                        .value(1));

    }

    @Test
    @DisplayName("Should search spaces")
    void shouldSearchSpaces() throws Exception {

        SpaceResponse response =
                new SpaceResponse(
                        1L,
                        "Sala IT",
                        SpaceType.MEETING_ROOM,
                        10,
                        "Nivel 5",
                        BigDecimal.TEN,
                        true);

        when(spaceService.search(
                true,
                SpaceType.MEETING_ROOM,
                10))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/spaces")
                                .param("active", "true")
                                .param("type", "MEETING_ROOM")
                                .param("capacity", "10"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.data.length()")
                        .value(1));

    }

}