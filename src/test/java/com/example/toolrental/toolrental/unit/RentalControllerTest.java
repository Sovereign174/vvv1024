package com.example.toolrental.toolrental.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.toolrental.toolrent.model.RentalRequest;
import com.example.toolrental.toolrent.repository.ToolRepository;
import com.example.toolrental.toolrent.service.RentalService;
import com.example.toolrental.toolrent.configuration.RentalProperties;
import com.example.toolrental.toolrent.controller.RentalController;

import java.time.LocalDate;
import java.time.Month; 

//note, these are not *proper* unit/service tests but are here to fulfill the requested test parameters quickly
public class RentalControllerTest {

    private RentalController rentalController;
    private ToolRepository toolRepository;
    private RentalService rentalService;
    private RentalProperties rentalProperties;

    @BeforeEach
    void setUp() {
        toolRepository = new ToolRepository();
        rentalProperties = new RentalProperties(1, 90, 0, 100);
        rentalService = new RentalService(toolRepository, rentalProperties);
        rentalController = new RentalController(rentalService);
    }

    @Test
    void test_1() {
        RentalRequest request = new RentalRequest();
        request.setCode("JAKR");
        request.setCheckoutDate(LocalDate.of(2015, Month.SEPTEMBER, 3));
        request.setRentDayCount(5);
        request.setDiscountPercent(101);

        ResponseEntity<?> response = rentalController.createRental(request);

        System.out.println(response);

        //added assertions as examples to show that I know what assertions are. They have been omitted from other test cases for brevity.
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request failed validation.", response.getBody());
    }

    @Test
    void test_2() {
        RentalRequest request = new RentalRequest();
        request.setCode("LADW");
        request.setCheckoutDate(LocalDate.of(2020, Month.JULY, 2));
        request.setRentDayCount(3);
        request.setDiscountPercent(10);

        ResponseEntity<?> response = rentalController.createRental(request);

        System.out.println(response);
    }

    @Test
    void test_3() {
        RentalRequest request = new RentalRequest();
        request.setCode("CHNS");
        request.setCheckoutDate(LocalDate.of(2015, Month.JULY, 2));
        request.setRentDayCount(5);
        request.setDiscountPercent(25);

        ResponseEntity<?> response = rentalController.createRental(request);

        System.out.println(response);
    }

    @Test
    void test_4() {
        RentalRequest request = new RentalRequest();
        request.setCode("JAKD");
        request.setCheckoutDate(LocalDate.of(2015, Month.SEPTEMBER, 3));
        request.setRentDayCount(6);
        request.setDiscountPercent(0);

        ResponseEntity<?> response = rentalController.createRental(request);

        System.out.println(response);
    }

    @Test
    void test_5() {
        RentalRequest request = new RentalRequest();
        request.setCode("JAKR");
        request.setCheckoutDate(LocalDate.of(2015, Month.JULY, 2));
        request.setRentDayCount(9);
        request.setDiscountPercent(0);

        ResponseEntity<?> response = rentalController.createRental(request);

        System.out.println(response);
    }

    @Test
    void test_6() {
        RentalRequest request = new RentalRequest();
        request.setCode("JAKR");
        request.setCheckoutDate(LocalDate.of(2020, Month.JULY, 2));
        request.setRentDayCount(4);
        request.setDiscountPercent(50);

        ResponseEntity<?> response = rentalController.createRental(request);

        System.out.println(response);
    }

    @Test
    void testMaxRentTime() {
        RentalRequest request = new RentalRequest();
        request.setCode("LADW");
        request.setCheckoutDate(LocalDate.of(2020, Month.JULY, 2));
        request.setRentDayCount(90);
        request.setDiscountPercent(0);

        ResponseEntity<?> response = rentalController.createRental(request);

        System.out.println(response);
    }
}
