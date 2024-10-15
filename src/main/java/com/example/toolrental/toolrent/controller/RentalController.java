package com.example.toolrental.toolrent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.toolrental.toolrent.model.RentalAgreement;
import com.example.toolrental.toolrent.model.RentalRequest;
import com.example.toolrental.toolrent.service.RentalService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/rental")
@AllArgsConstructor
public class RentalController {

    private RentalService rentalService;

    @PostMapping("/create")
    public ResponseEntity<?> createRental(@RequestBody RentalRequest request) {
        try {
            if(rentalService.isValidRentalRequest(request)) {
                RentalAgreement agreement = rentalService.generateRentalAgreement(request);
                log.info("Rental Agreement: \n" + agreement.getFormattedRentalAgreement());
                return ResponseEntity.ok(agreement);
            } else {
                return getExceptionResponse(new IllegalStateException("Request failed validation."));
            }
        } catch (Exception ex) {
            return getExceptionResponse(ex);
        }
    }

    private ResponseEntity<?> getExceptionResponse(Exception ex) {
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
    }
}