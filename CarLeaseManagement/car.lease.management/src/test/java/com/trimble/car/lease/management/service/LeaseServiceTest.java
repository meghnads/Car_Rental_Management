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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaseServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private EndCustomerRepository customerRepository;

    @Mock
    private LeaseRepository leaseRepository;

    @InjectMocks
    private LeaseService leaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testStartLease_CustomerNotFound() {
        LeaseRequestDto request = new LeaseRequestDto();

        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> leaseService.startLease(request));
    }

    @Test
    void testStartLease_TooManyActiveLeases() {
        LeaseRequestDto request = new LeaseRequestDto();
        Car car = new Car();
        car.setId(1L);
        car.setStatus("Available");

        EndCustomer customer = new EndCustomer();
        customer.setId(1L);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(leaseRepository.countByCustomerIdAndEndDateIsNull(1L)).thenReturn(2L);

        assertThrows(RuntimeException.class, () -> leaseService.startLease(request));
    }

    @Test
    void testEndLease_Success() {
        Lease lease = new Lease();
        lease.setId(1L);
        lease.setStartDate(LocalDate.now().minusDays(1));
        lease.setCar(new Car());

        when(leaseRepository.findById(1L)).thenReturn(Optional.of(lease));

        leaseService.endLease(1L);

        assertNotNull(lease.getEndDate());
        verify(leaseRepository, times(1)).save(lease);
        verify(carRepository, times(1)).save(lease.getCar());
    }

    @Test
    void testEndLease_LeaseNotFound() {
        when(leaseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> leaseService.endLease(1L));
    }

    @Test
    void testGetCustomerHistory() {
        Long customerId = 1L;
        Lease lease1 = new Lease();

        Lease lease2 = new Lease();

        when(leaseRepository.findByCustomerId(customerId)).thenReturn(Arrays.asList(lease1, lease2));

        List<LeaseHistoryDto> leaseHistory = leaseService.getCustomerHistory(customerId);

        assertEquals(2, leaseHistory.size());
    }
}