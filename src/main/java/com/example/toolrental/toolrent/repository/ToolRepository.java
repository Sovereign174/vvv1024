package com.example.toolrental.toolrent.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.toolrental.toolrent.model.ToolData;
import com.example.toolrental.toolrent.model.ToolType;

public class ToolRepository {
    private final Map<String, ToolData> toolData = new HashMap<>();
    //              Tool code, Tool Data

    public ToolRepository() {
        // Hardcoded tool data initialization, using builder pattern for improved readability
        toolData.put("CHNS", ToolData.builder()
                                        .code("CHNS")
                                        .type(ToolType.Chainsaw)
                                        .brand("Stihl")
                                        .dailyCharge(new BigDecimal("1.49"))
                                        .weekdayCharge(true)
                                        .weekendCharge(false)
                                        .holidayCharge(true)
                                        .build()); // Chainsaw

        toolData.put("LADW", ToolData.builder()
                                        .code("LADW")
                                        .type(ToolType.Ladder)
                                        .brand("Werner")
                                        .dailyCharge(new BigDecimal("1.99"))
                                        .weekdayCharge(true)
                                        .weekendCharge(true)
                                        .holidayCharge(false)
                                        .build()); // Ladder

        toolData.put("JAKD", ToolData.builder()
                                        .code("JAKD")
                                        .type(ToolType.Jackhammer)
                                        .brand("DeWalt")
                                        .dailyCharge(new BigDecimal("2.99"))
                                        .weekdayCharge(true)
                                        .weekendCharge(false)
                                        .holidayCharge(false)
                                        .build()); // Jackhammer

        toolData.put("JAKR", ToolData.builder()
                                        .code("JAKR")
                                        .type(ToolType.Jackhammer)
                                        .brand("Ridgid")
                                        .dailyCharge(new BigDecimal("2.99"))
                                        .weekdayCharge(true)
                                        .weekendCharge(false)
                                        .holidayCharge(false)
                                        .build()); // Jackhammer
    }

    public Optional<ToolData> findToolByCode(String toolCode) {
        return Optional.ofNullable(toolData.get(toolCode));
    }
}