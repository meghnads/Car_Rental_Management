package com.trimble.car.lease.management.service;

import com.trimble.car.lease.management.dto.LeaseRequestDto;
import com.trimble.car.lease.management.dto.LeaseHistoryDto;
import com.trimble.car.lease.management.model.Car;
import com.trimble.car.lease.management.model.EndCustomer;
import com.trimble.car.lease.management.model.Lease;
import com.trimble.car.lease.management.repository.CarRepository;
import com.trimble.car.lease.management.repository.EndCustomerRepository;
import com.trimble.car.lease.management.repository.LeaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaseService {

    private final CarRepository carRepository;
    private final EndCustomerRepository customerRepository;
    private final LeaseRepository leaseRepository;

    public void startLease(LeaseRequestDto request) {
            if (!customerRepository.existsById(request.getCustomerId())) {
                throw new IllegalArgumentException("Customer ID does not exist: " + request.getCustomerId());
            }
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        EndCustomer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        long activeLeases = leaseRepository.countByCustomerIdAndEndDateIsNull(request.getCustomerId());
        if (activeLeases >= 2) {
            throw new RuntimeException("Customer already has 2 active leases");
        }

        Lease lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
        leaseRepository.save(lease);
        lease.setStartDate(LocalDate.now(ZoneId.systemDefault()));
        lease.setEndDate(LocalDate.now(ZoneId.systemDefault()));
        car.setStatus(lease.getStartDate().isAfter(LocalDate.now()) ? "Scheduled" : "Leased");
        carRepository.save(car);
    }

    public void endLease(Long leaseId) {
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new EntityNotFoundException("Lease not found"));
        lease.setEndDate(LocalDate.now(ZoneId.systemDefault()));
        leaseRepository.save(lease);

        Car car = lease.getCar();
        car.setStatus(lease.getEndDate().isAfter(LocalDate.now()) ? "Scheduled" : "Idle");
        carRepository.save(car);
    }

    public List<LeaseHistoryDto> getCustomerHistory(Long customerId) {
        List<Lease> leases = leaseRepository.findByCustomerId(customerId);
        return leases.stream()
                .map(lease -> new LeaseHistoryDto())
                .collect(Collectors.toList());
    }
}

