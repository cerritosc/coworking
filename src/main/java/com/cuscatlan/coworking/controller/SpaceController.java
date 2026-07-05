package com.cuscatlan.coworking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.cuscatlan.coworking.common.response.ApiResponse;
import com.cuscatlan.coworking.enums.SpaceType;
import com.cuscatlan.coworking.dto.request.space.CreateSpaceRequest;
import com.cuscatlan.coworking.dto.request.space.UpdateSpaceRequest;
import com.cuscatlan.coworking.dto.response.space.SpaceResponse;
import com.cuscatlan.coworking.service.SpaceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spaces")
@Tag(
        name = "Spaces",
        description = "Operations related to coworking spaces"
)
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    @Operation(summary = "Create a new coworking space")
    public ResponseEntity<ApiResponse<SpaceResponse>> create(
            @Valid @RequestBody CreateSpaceRequest request) {

        SpaceResponse response = spaceService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Space created successfully.",
                        response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing space")
    public ResponseEntity<ApiResponse<SpaceResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSpaceRequest request) {

        SpaceResponse response =
                spaceService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Space updated successfully.",
                        response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a space by id")
    public ResponseEntity<ApiResponse<SpaceResponse>> findById(
            @PathVariable Long id) {

        SpaceResponse response =
                spaceService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Space retrieved successfully.",
                        response));
    }

    @GetMapping
    @Operation(summary = "Search coworking spaces")
    public ResponseEntity<ApiResponse<List<SpaceResponse>>> search(

            @RequestParam(required = false)
            Boolean active,

            @RequestParam(required = false)
            SpaceType type,

            @RequestParam(required = false)
            Integer capacity) {

        List<SpaceResponse> response =
                spaceService.search(
                        active,
                        type,
                        capacity);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Spaces retrieved successfully.",
                        response));
    }

}