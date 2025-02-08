package com.trimble.car.lease.management.controller;

import com.trimble.car.lease.management.dto.CarDto;
import com.trimble.car.lease.management.dto.LeaseHistoryDto;
import com.trimble.car.lease.management.service.CarService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @GetMapping("/all")
    public List<CarDto> getAllCars() {
        logger.info("Request to get all cars");
        return carService.getAllCars();
    }

    @PostMapping("/register")
    public CarDto registerCar(@RequestBody CarDto carDto) {
        logger.info("Request to register a car: {}", carDto);
        if (carDto.getYear() == null) {
            throw new IllegalArgumentException("Year is required");
        }
        return carService.registerCar(carDto);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getCarStatus(@PathVariable Long id) {
        logger.info("Request to get status of car with id: {}", id);
        String status = carService.getCarStatus(id);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateCarStatus(@PathVariable Long id, @RequestBody String status) {
        logger.info("Request to update status of car with id: {} to {}", id, status);
        carService.updateCarStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<CarDto> getCarsByStatus(@PathVariable String status) {
        logger.info("Request to get cars by status: {}", status);
        return carService.getCarsByStatus(status);
    }

    @GetMapping("/{id}/lease-history")
    public List<LeaseHistoryDto> getLeaseHistory(@PathVariable Long id) {
        logger.info("Request to get lease history of car with id: {}", id);
        return carService.getLeaseHistory(id);
    }
}

