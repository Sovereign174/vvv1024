package com.example.toolrental.toolrent.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ChargingData {
    int daysCharged;
    BigDecimal preDiscountCharge;
    BigDecimal discountAmount;
    BigDecimal finalCharge;
}
