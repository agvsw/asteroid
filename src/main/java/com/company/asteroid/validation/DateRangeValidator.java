package com.company.asteroid.validation;

import com.company.asteroid.constants.ValidationConstants;
import com.company.asteroid.exception.InvalidDateRangeException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class DateRangeValidator {
    public void validate(
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate == null || endDate == null) {
            throw new InvalidDateRangeException(
                    "Start date and end date are required"
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new InvalidDateRangeException(
                    "End date must not be before start date"
            );
        }

        long days = ChronoUnit.DAYS.between(
                startDate,
                endDate
        );

        if (days > ValidationConstants.MAX_DATE_RANGE_DAYS) {
            throw new InvalidDateRangeException(
                    String.format(
                            "Date range must not exceed %d days",
                            ValidationConstants.MAX_DATE_RANGE_DAYS
                    )
            );
        }
    }
}
