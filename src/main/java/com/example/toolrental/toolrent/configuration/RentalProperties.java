package com.example.toolrental.toolrent.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "rental")
public class RentalProperties {
    private final int minimumRentDays;
    private final int maximumRentDays;
    private final int minimumDiscountPercent;
    private final int maximumDiscountPercent;
}
