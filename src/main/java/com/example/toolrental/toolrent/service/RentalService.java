package com.example.toolrental.toolrent.service;

import com.example.toolrental.toolrent.model.ChargingData;
import com.example.toolrental.toolrent.model.RentalAgreement;
import com.example.toolrental.toolrent.model.RentalRequest;
import com.example.toolrental.toolrent.model.ToolData;
import com.example.toolrental.toolrent.repository.ToolRepository;
import com.example.toolrental.toolrent.configuration.RentalProperties;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Month; 
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalService {
    private static final int DAYS_IN_WEEK = 7;
    private static final int WEEKDAYS_IN_WEEK = 5;
    private static final int WEEKENDS_IN_WEEK = 2;
    private static final int DECIMAL_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final ToolRepository toolRepository;
    private final RentalProperties rentalProperties;

    public boolean isValidRentalRequest(RentalRequest request) {
        String toolCode = request.getCode();

        log.info("Received rental request: " + request);

        if(StringUtils.hasText(toolCode)) {
            return toolRepository.findToolByCode(toolCode)
            .filter(tool -> validateRentalRequestDays(request))
            .filter(tool -> validateRentalRequestDiscount(request))
            .isPresent();
        } else {
            //Ideally this will have a UUID throughout for comprehensive tracking, but that is excluded for brevity
            log.error("Request has failed validation.");
            return false;
        }
    }

    private boolean validateRentalRequestDays(RentalRequest request) {
        if(request.getRentDayCount() >= rentalProperties.getMinimumRentDays() && 
        request.getRentDayCount() <= rentalProperties.getMaximumRentDays()) {
            return true;
        } else {
            log.error("Request has failed validation due to rental days: " + request.getRentDayCount() +
             " being out of range: " + rentalProperties.getMinimumRentDays() + " -> " + rentalProperties.getMaximumRentDays());
            return false;
        }
    }

    private boolean validateRentalRequestDiscount(RentalRequest request) {
        if(request.getDiscountPercent() >= rentalProperties.getMinimumDiscountPercent() && 
        request.getDiscountPercent() <= rentalProperties.getMaximumDiscountPercent()) {
            return true;
         } else {
            log.error("Request has failed validation due to request discount percent: " + request.getDiscountPercent() +
             " being out of range: " + rentalProperties.getMinimumDiscountPercent() + " - " + rentalProperties.getMaximumDiscountPercent());
            return false;
         }
    }

    public RentalAgreement generateRentalAgreement(RentalRequest request) {
        RentalAgreement rentalAgreement = new RentalAgreement();
        ToolData toolData = toolRepository.findToolByCode(request.getCode())
        .orElseThrow(() -> new IllegalStateException("ToolData should not be null after validation."));
        //this has already been validated as not null

        ChargingData chargeData = calculateChargingData(request, toolData);

        rentalAgreement.setCode(toolData.getCode());
        rentalAgreement.setType(toolData.getType().toString());
        rentalAgreement.setBrand(toolData.getBrand());
        rentalAgreement.setRentalDays(request.getRentDayCount());
        rentalAgreement.setCheckoutDate(request.getCheckoutDate());
        rentalAgreement.setDueDate(calculateDueDate(request.getCheckoutDate(), request.getRentDayCount()));
        rentalAgreement.setDailyCharge(toolData.getDailyCharge());
        rentalAgreement.setDaysCharged(chargeData.getDaysCharged());
        rentalAgreement.setPreDiscountCharge(chargeData.getPreDiscountCharge());
        rentalAgreement.setDiscountPercent(String.valueOf(request.getDiscountPercent()));
        rentalAgreement.setDiscountAmount(chargeData.getDiscountAmount());
        rentalAgreement.setFinalCharge(chargeData.getFinalCharge());

        log.info("Charging data for request: " + request + "\n" + toolData + "\n" + chargeData);

        return rentalAgreement;
    }

    //ideally, this function would also consider configurable edge cases such as
    //can the due date be on a holiday? or should it be moved forward/backward
    //is the due date during the business days of the bussiness? or should it be moved forward/backward
    private LocalDate calculateDueDate(LocalDate checkoutDate, int rentalDays) {
        return checkoutDate.plusDays(rentalDays);
    }

    /**Calculates the the final and intermediate pricing based on the days of the week
     * within the rental period, and the tool specific configurations for charging info.
     * 
     * @param request the rental request received from the api call
     * @param toolData the retrieved tool data from the repository containing data needed for accurate charging
     * @return the ChargingData containing the computed fields necessary for the rental agreement.
     */
    private ChargingData calculateChargingData(RentalRequest request, ToolData toolData) {
        ChargingData chargingData = new ChargingData();
        LocalDate checkoutDate = request.getCheckoutDate();
        int oddDaysInLastWeek = request.getRentDayCount() % DAYS_IN_WEEK;
        int numWeekdays = 0;
        int numWeekends = 0;
        int numHolidays = 0;
        int daysCharged = 0;

        BigDecimal dailyCharge = toolData.getDailyCharge();
        BigDecimal discountPercent = new BigDecimal(String.valueOf(request.getDiscountPercent())).divide(new BigDecimal("100"));
        BigDecimal preDiscountCharge = BigDecimal.ZERO;

        //calculalate basic weekdays/weekends since we know how many there are in any 7 day span
        if(request.getRentDayCount() > DAYS_IN_WEEK) {
            int numWeeks = request.getRentDayCount() / DAYS_IN_WEEK;

            numWeekdays = numWeeks * WEEKDAYS_IN_WEEK;
            numWeekends = numWeeks * WEEKENDS_IN_WEEK;
        }

        //calculate weekdays/weekends that are outside of a basic 7 day span
        if(oddDaysInLastWeek > 0) {
            LocalDate oddDayIterator = checkoutDate.plusDays((request.getRentDayCount() - oddDaysInLastWeek) + 1);
            //plus 1 because we only start charging from the day after checkout

            for(int dayCount = 0; dayCount < oddDaysInLastWeek; dayCount++) {
                int dayOfWeekEnumValue = oddDayIterator.getDayOfWeek().getValue();

                if(dayOfWeekEnumValue >= DayOfWeek.MONDAY.getValue() && dayOfWeekEnumValue <= DayOfWeek.FRIDAY.getValue()) {
                    numWeekdays++;
                } else if(dayOfWeekEnumValue == DayOfWeek.SATURDAY.getValue() || dayOfWeekEnumValue == DayOfWeek.SUNDAY.getValue()) {
                    numWeekends++;
                }
                
                oddDayIterator = oddDayIterator.plusDays(1);
            }
        }

        numHolidays += getNumberOfHolidays(request);

        if(toolData.isWeekdayCharge()) {
            preDiscountCharge = preDiscountCharge.add(dailyCharge.multiply(new BigDecimal(String.valueOf(numWeekdays))));
            daysCharged += numWeekdays;
        }

        if(toolData.isWeekendCharge()) {
            preDiscountCharge = preDiscountCharge.add(dailyCharge.multiply(new BigDecimal(String.valueOf(numWeekends))));
            daysCharged += numWeekends;
        }

        if(!toolData.isHolidayCharge()) {
            preDiscountCharge = preDiscountCharge.subtract(dailyCharge.multiply(new BigDecimal(String.valueOf(numHolidays))));
            daysCharged -= numHolidays;
        }

        log.info("There were: " + numWeekdays + " weekdays.");
        log.info("There were: " + numWeekends + " weekends.");
        log.info("There were: " + numHolidays + " holidays.");

        //it is assumed that setting scale after calculations is more desirable than before as it allows insignificant intermediate precision
        //values to work their way up to being significant when it is time for rounding to occur.
        chargingData.setPreDiscountCharge(preDiscountCharge.setScale(DECIMAL_SCALE, ROUNDING_MODE));
        chargingData.setDaysCharged(daysCharged);
        chargingData.setDiscountAmount(preDiscountCharge.multiply(discountPercent).setScale(DECIMAL_SCALE, ROUNDING_MODE));
        chargingData.setFinalCharge(preDiscountCharge.subtract(chargingData.getDiscountAmount()).setScale(DECIMAL_SCALE));

        return chargingData;
    }

    /**
     * Calculates the number of holidays that fall within the rental period.
     * Assumes all holidays are within the same year as the checkout date.
     * Can be augmented to check for a holiday for each year easily, but omitted for brevity.
     *
     * @param request the rental request containing the checkout date and rental duration
     * @return the number of holidays that fall within the rental period
     */
    private int getNumberOfHolidays(RentalRequest request) {
        LocalDate startDate = request.getCheckoutDate();
        LocalDate endDate = startDate.plusDays(request.getRentDayCount());
        int numHolidays = 0;

        List<LocalDate> holidayDates = new ArrayList<>();
        holidayDates.add(getLaborDayDate(request));
        holidayDates.add(getIndependenceDayDate(request));

        for(LocalDate holidayDate : holidayDates) {
            if((holidayDate.isAfter(startDate) && holidayDate.isBefore(endDate)) || endDate.isEqual(holidayDate)) {
                numHolidays++;
            }
        }

        return numHolidays;
    }

    //for brevity, it is assumed that the start/end date are in the same year. Normally I imagine this would not be the case and
    //you'd need to determine the holiday for each year during which the rental is checked out, so this would return an
    //array of LocalDates in that case
    private LocalDate getLaborDayDate(RentalRequest request) {
        //get the first Monday in september of the year the rental starts
        LocalDate dayInSeptember = LocalDate.of(request.getCheckoutDate().getYear(), Month.SEPTEMBER, 1);

        //iterate for a week until you find the first Monday in september
        for(int i = 0; i < DAYS_IN_WEEK; i++) {
            log.debug("Checking day: " + dayInSeptember.toString() + " which is a: " + dayInSeptember.getDayOfWeek().name());

            if(dayInSeptember.getDayOfWeek() == DayOfWeek.MONDAY) {
                return dayInSeptember;
            }

            dayInSeptember = dayInSeptember.plusDays(1);
        }

        throw new IllegalStateException("The first Monday in September should always be found for a request: " + request);
    }

    private LocalDate getIndependenceDayDate(RentalRequest request) {
        LocalDate fourthOfJuly = LocalDate.of(request.getCheckoutDate().getYear(), Month.JULY, 4);

        if(fourthOfJuly.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return fourthOfJuly.minusDays(1);
        } else if(fourthOfJuly.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return fourthOfJuly.plusDays(1);
        }

        return fourthOfJuly;
    }
}
