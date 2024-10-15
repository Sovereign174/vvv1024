package com.example.toolrental.toolrent.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RentalRequest {
    private String code;
    private int rentDayCount;
    private int discountPercent;
    private LocalDate checkoutDate;
}
