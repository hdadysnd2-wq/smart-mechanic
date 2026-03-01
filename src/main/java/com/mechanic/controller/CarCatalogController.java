package com.mechanic.controller;

import com.mechanic.dto.response.ApiResponse;
import com.mechanic.dto.response.CarSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarCatalogController {

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarSummaryResponse>>> getAllCars() {
        var cars = List.of(
                new CarSummaryResponse("1", "1HGBH41JXMN109186", "Toyota Camry 2022",
                        "Toyota", "Camry", 2022, "Gasoline", 203),
                new CarSummaryResponse("2", "2T1BURHE0JC034264", "Honda Civic 2021",
                        "Honda", "Civic", 2021, "Gasoline", 158)
        );
        return ResponseEntity.ok(ApiResponse.ok(cars));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarSummaryResponse>> getCarById(@PathVariable String id) {
        var car = new CarSummaryResponse(id, "1HGBH41JXMN109186",
                "Toyota Camry 2022", "Toyota", "Camry", 2022, "Gasoline", 203);
        return ResponseEntity.ok(ApiResponse.ok(car));
    }
}
