package com.trimble.car.lease.management.controller;

import com.trimble.car.lease.management.dto.CarDto;
import com.trimble.car.lease.management.dto.LeaseHistoryDto;
import com.trimble.car.lease.management.model.EndCustomer;
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
@RequestMapping("/api/end-customer")
@RequiredArgsConstructor
public class EndCustomerController {

    private final EndCustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(EndCustomerController.class);

    @PostMapping("/register")
    public ResponseEntity<EndCustomer> registerCustomer(@RequestBody EndCustomer customer) {
        logger.info("Request to register a customer: {}", customer);
        return ResponseEntity.ok(customerService.registerCustomer(customer));
    }

    @GetMapping("/cars")
    public List<CarDto> getAllCars() {
        logger.info("Request to get all cars");
        return customerService.getAllCars();
    }

    @PostMapping("/lease/start")
    public ResponseEntity<String> startLease(@RequestParam Long carId, @RequestParam Long customerId) {
        logger.info("Request to start lease for carId: {} and customerId: {}", carId, customerId);
        customerService.startLease(carId, customerId);
        return ResponseEntity.ok("Lease started successfully");
    }

    @PostMapping("/lease/end")
    public ResponseEntity<String> endLease(@RequestParam Long leaseId) {
        logger.info("Request to end lease with leaseId: {}", leaseId);
        customerService.endLease(leaseId);
        return ResponseEntity.ok("Lease ended successfully");
    }

    @GetMapping("/lease-history/{customerId}")
    public List<LeaseHistoryDto> getCustomerLeaseHistory(@PathVariable Long customerId) {
        logger.info("Request to get lease history for customerId: {}", customerId);
        return customerService.getCustomerLeaseHistory(customerId);
    }
}
