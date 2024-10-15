package com.example.toolrental.toolrent.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ToolData {
    private String code;
    private ToolType type;
    private String brand;
    private BigDecimal dailyCharge;

    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;
}
