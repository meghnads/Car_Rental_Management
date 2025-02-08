package com.trimble.car.lease.management.controller;

import com.trimble.car.lease.management.dto.CarDto;
import com.trimble.car.lease.management.dto.LeaseHistoryDto;
import com.trimble.car.lease.management.model.EndCustomer;
import com.trimble.car.lease.management.service.CarService;
import com.trimble.car.lease.management.service.EndCustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CarService carService;
    private final EndCustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/all-cars")
    public List<CarDto> getAllCars() {
        logger.info("Fetching all cars for the admin");
        return carService.getAllCars();
    }

    @PostMapping("/register-car")
    public ResponseEntity<String> registerCar(@RequestBody CarDto carDto) {
        carService.registerCar(carDto);
        return ResponseEntity.ok("Car registered successfully by Admin!");
    }

    @GetMapping("/status/{carId}")
    public ResponseEntity<String> getCarStatus(@PathVariable Long carId) {
        return ResponseEntity.ok(carService.getCarStatus(carId));
    }

    @GetMapping("/lease-history/{carId}")
    public List<LeaseHistoryDto> getCarLeaseHistory(@PathVariable Long carId) {
        return carService.getLeaseHistory(carId);
    }

    @PostMapping("/register-customer")
    public ResponseEntity<EndCustomer> registerCustomer(@RequestBody EndCustomer customer) {
        return ResponseEntity.ok(customerService.registerCustomer(customer));
    }

    @GetMapping("/all-customers")
    public List<EndCustomer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/start-lease")
    public ResponseEntity<String> startLease(@RequestParam Long carId, @RequestParam Long customerId) {
        customerService.startLease(carId, customerId);
        return ResponseEntity.ok("Lease started successfully by Admin!");
    }

    @PostMapping("/end-lease")
    public ResponseEntity<String> endLease(@RequestParam Long leaseId) {
        customerService.endLease(leaseId);
        return ResponseEntity.ok("Lease ended successfully by Admin!");
    }

    @PostMapping("/update-car-status/{carId}")
    public ResponseEntity<String> updateCarStatus(@PathVariable Long carId, @RequestParam String status) {
        carService.updateCarStatus(carId, status);
        return ResponseEntity.ok("Car status updated by Admin!");
    }
}


