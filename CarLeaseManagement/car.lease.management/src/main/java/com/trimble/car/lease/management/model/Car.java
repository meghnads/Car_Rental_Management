package com.trimble.car.lease.management.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private String status;
    private Integer year;// Idle, On Lease, On Service

    @ManyToOne
    private Owner owner;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<com.trimble.car.lease.management.model.Lease> leaseHistory;
}
