package com.trimble.car.lease.management.service;

import com.trimble.car.lease.management.dto.CarDto;
import com.trimble.car.lease.management.model.EndCustomer;
import com.trimble.car.lease.management.repository.CarRepository;
import com.trimble.car.lease.management.model.Car;
import com.trimble.car.lease.management.repository.EndCustomerRepository;
import com.trimble.car.lease.management.repository.LeaseRepository;
import com.trimble.car.lease.management.dto.LeaseHistoryDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final LeaseRepository leaseRepository;
    private final EndCustomerRepository customerRepository;

    public CarDto registerCar(CarDto carDto) {
        Car car = new Car();
        car.setMake(carDto.getMake());
        car.setModel(carDto.getModel());
        car.setYear(carDto.getYear());
        car.setStatus(carDto.getStatus());
        carRepository.save(car);
        return new CarDto(car.getId(), car.getMake(), car.getModel(), car.getYear(), car.getStatus());
    }

    public String getCarStatus(Long carId) {
        return carRepository.findById(carId)
                .map(Car::getStatus)
                .orElse("Car not found");
    }

    public List<LeaseHistoryDto> getLeaseHistory(Long carId) {
        return leaseRepository.findByCarId(carId).stream()
                .map(lease -> {
                    LeaseHistoryDto dto = new LeaseHistoryDto();
                    dto.setStartDate(lease.getStartDate());
                    dto.setEndDate(lease.getEndDate());
                    return dto;
                }).collect(Collectors.toList());
    }

    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(car -> new CarDto(car.getId(), car.getMake(), car.getModel(), car.getYear(), car.getStatus()))
                .collect(Collectors.toList());
    }

    public void updateCarStatus(Long carId, String status) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        car.setStatus(status);
        carRepository.save(car);
    }

    public List<CarDto> getCarsByStatus(String status) {
        List<Car> cars = carRepository.findByStatus(status);
        return cars.stream()
                .map(car -> new CarDto(car.getId(), car.getMake(), car.getModel(), car.getYear(), car.getStatus()))
                .collect(Collectors.toList());
    }

    // Register a customer to a car
    public void registerCustomerToCar(Long carId, Long customerId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        EndCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        car.setStatus("Leased");
        carRepository.save(car);
    }
}


