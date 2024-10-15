package com.example.toolrental.toolrent.model;

import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.text.NumberFormat;
import java.math.BigDecimal;

@Data
public class RentalAgreement {
    private String code;
    private String type;
    private String brand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private BigDecimal dailyCharge;
    private int daysCharged;
    private BigDecimal preDiscountCharge;
    private String discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public String getFormattedRentalAgreement() {
        StringBuilder builder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        NumberFormat usdFormat = NumberFormat.getCurrencyInstance();

        builder.append("Tool Code: " + code + "\n");
        builder.append("Tool Type: " + type + "\n");
        builder.append("Tool Brand: " + brand + "\n");
        builder.append("Rental Days: " + rentalDays + "\n");
        builder.append("Check out date: " + checkoutDate.format(formatter) + "\n");
        builder.append("Due date: " + dueDate.format(formatter) + "\n");
        builder.append("Daily rental charge: " + usdFormat.format(dailyCharge) + "\n");
        builder.append("Charge days: " + daysCharged + "\n");
        builder.append("Pre-discount charge: " + usdFormat.format(preDiscountCharge) + "\n");
        builder.append("Discount percent: " + discountPercent + "%\n");
        builder.append("Discount amount: " + usdFormat.format(discountAmount) + "\n");
        builder.append("Final Charge: " + usdFormat.format(finalCharge));

        return builder.toString();
    }
}
