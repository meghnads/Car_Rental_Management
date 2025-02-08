package com.trimble.car.lease.management.dto;

public class CarDto {
    private Long id;
    private String make;
    private String model;
    private Integer year;
    private String status;

    public CarDto(Long id, String make, String model, Integer year, String status) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
