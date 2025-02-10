package com.opensource.resturantfinder.controller;

import com.opensource.resturantfinder.common.ApiResponse;
import com.opensource.resturantfinder.entity.Restaurant;
import com.opensource.resturantfinder.model.RestaurantDetailsResponse;
import com.opensource.resturantfinder.model.RestaurantRequest;
import com.opensource.resturantfinder.security.JwtUtil;
import com.opensource.resturantfinder.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant", description = "Restaurant management API")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final JwtUtil jwtUtil;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, JwtUtil jwtUtil) {
        this.restaurantService = restaurantService;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping
    @Operation(summary = "Add a new restaurant", description = "Creates a new restaurant entry")
    public ResponseEntity<ApiResponse<Restaurant>> addRestaurant(
            @Parameter(description = "Restaurant details", required = true)
            @Valid @RequestBody RestaurantRequest restaurantRequest,
            @Parameter(description = "Unique request identifier", required = true)
            @RequestHeader("X-Request-ID") String requestId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);
        Restaurant savedRestaurant = restaurantService.addRestaurant(restaurantRequest,email);
        return ResponseEntity.ok(ApiResponse.success(savedRestaurant, requestId));
    }


    @GetMapping("/{restaurantId}")
    @Operation(summary = "Get Restaurant Details", description = "Fetch details and reviews for a restaurant")
    public ResponseEntity<ApiResponse<RestaurantDetailsResponse>> getRestaurantDetails(
            @PathVariable Long restaurantId,
            @RequestParam(value = "sortBy", required = false, defaultValue = "recent") String sortBy,
            @RequestHeader("X-Request-ID") String requestId) {
        RestaurantDetailsResponse response = restaurantService.getRestaurantDetails(restaurantId, sortBy);
        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

}