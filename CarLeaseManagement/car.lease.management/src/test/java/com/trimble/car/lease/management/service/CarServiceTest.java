package com.trimble.car.lease.management.service;

import com.trimble.car.lease.management.dto.CarDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private LeaseRepository leaseRepository;

    @Mock
    private EndCustomerRepository customerRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCar() {
        CarDto carDto = new CarDto(null, "Toyota", "Camry", 2022, "Available");
        Car car = new Car();
        car.setId(1L);
        car.setMake("Toyota");
        car.setModel("Camry");
        car.setYear(2022);
        car.setStatus("Available");

        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarDto registeredCar = carService.registerCar(carDto);

        assertEquals(null, registeredCar.getId());
        assertEquals("Toyota", registeredCar.getMake());
        assertEquals("Camry", registeredCar.getModel());
        assertEquals(2022, registeredCar.getYear());
        assertEquals("Available", registeredCar.getStatus());
    }

    @Test
    void testGetCarStatus() {
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);
        car.setStatus("Available");

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        String status = carService.getCarStatus(carId);

        assertEquals("Available", status);
    }

    @Test
    void testGetCarStatus_CarNotFound() {
        Long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        String status = carService.getCarStatus(carId);

        assertEquals("Car not found", status);
    }

    @Test
    void testGetLeaseHistory() {
        Long carId = 1L;
        Lease lease1 = new Lease();
        lease1.setStartDate(LocalDate.of(2025, 2, 7));
        lease1.setEndDate(LocalDate.of(2025, 2, 10));

        Lease lease2 = new Lease();
        lease2.setStartDate(LocalDate.of(2025, 2, 8));
        lease2.setEndDate(LocalDate.of(2025, 2, 9));

        when(leaseRepository.findByCarId(carId)).thenReturn(Arrays.asList(lease1, lease2));

        List<LeaseHistoryDto> leaseHistory = carService.getLeaseHistory(carId);

        assertEquals(2, leaseHistory.size());
        assertEquals(lease1.getStartDate(), leaseHistory.get(0).getStartDate());
        assertEquals(lease1.getEndDate(), leaseHistory.get(0).getEndDate());
        assertEquals(lease2.getStartDate(), leaseHistory.get(1).getStartDate());
        assertEquals(lease2.getEndDate(), leaseHistory.get(1).getEndDate());
    }

    @Test
    void testGetAllCars() {
        Car car1 = new Car();
        Car car2 = new Car();

        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<CarDto> cars = carService.getAllCars();

        assertEquals(2, cars.size());
        assertEquals(car1.getId(), cars.get(0).getId());
        assertEquals(car1.getMake(), cars.get(0).getMake());
        assertEquals(car1.getModel(), cars.get(0).getModel());
        assertEquals(car1.getYear(), cars.get(0).getYear());
        assertEquals(car1.getStatus(), cars.get(0).getStatus());

        assertEquals(car2.getId(), cars.get(1).getId());
        assertEquals(car2.getMake(), cars.get(1).getMake());
        assertEquals(car2.getModel(), cars.get(1).getModel());
        assertEquals(car2.getYear(), cars.get(1).getYear());
        assertEquals(car2.getStatus(), cars.get(1).getStatus());
    }

    @Test
    void testUpdateCarStatus() {
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);
        car.setStatus("Available");

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        carService.updateCarStatus(carId, "Leased");

        assertEquals("Leased", car.getStatus());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testUpdateCarStatus_CarNotFound() {
        Long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carService.updateCarStatus(carId, "Leased"));
    }

    @Test
    void testGetCarsByStatus() {
        Car car1 = new Car();
        Car car2 = new Car();

        when(carRepository.findByStatus("Available")).thenReturn(Arrays.asList(car1, car2));

        List<CarDto> cars = carService.getCarsByStatus("Available");

        assertEquals(2, cars.size());
        assertEquals(car1.getId(), cars.get(0).getId());
        assertEquals(car1.getMake(), cars.get(0).getMake());
        assertEquals(car1.getModel(), cars.get(0).getModel());
        assertEquals(car1.getYear(), cars.get(0).getYear());
        assertEquals(car1.getStatus(), cars.get(0).getStatus());

        assertEquals(car2.getId(), cars.get(1).getId());
        assertEquals(car2.getMake(), cars.get(1).getMake());
        assertEquals(car2.getModel(), cars.get(1).getModel());
        assertEquals(car2.getYear(), cars.get(1).getYear());
        assertEquals(car2.getStatus(), cars.get(1).getStatus());
    }

    @Test
    void testRegisterCustomerToCar() {
        Long carId = 1L;
        Long customerId = 1L;
        Car car = new Car();
        car.setId(carId);
        car.setStatus("Available");

        EndCustomer customer = new EndCustomer();
        customer.setId(customerId);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        carService.registerCustomerToCar(carId, customerId);

        assertEquals("Leased", car.getStatus());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testRegisterCustomerToCar_CarNotFound() {
        Long carId = 1L;
        Long customerId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.registerCustomerToCar(carId, customerId));
    }

    @Test
    void testRegisterCustomerToCar_CustomerNotFound() {
        Long carId = 1L;
        Long customerId = 1L;
        Car car = new Car();
        car.setId(carId);
        car.setStatus("Available");

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.registerCustomerToCar(carId, customerId));
    }

}