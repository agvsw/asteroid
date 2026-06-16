package com.company.asteroid.validation;

import com.company.asteroid.constants.ValidationConstants;
import com.company.asteroid.exception.InvalidDateRangeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DateRangeValidatorTest {
    private final DateRangeValidator validator =
            new DateRangeValidator();

    @Test
    void shouldThrowExceptionWhenStartDateIsNull() {

        InvalidDateRangeException exception =
                assertThrows(
                        InvalidDateRangeException.class,
                        () -> validator.validate(
                                null,
                                LocalDate.now()
                        )
                );

        assertEquals(
                "Start date and end date are required",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsNull() {

        InvalidDateRangeException exception =
                assertThrows(
                        InvalidDateRangeException.class,
                        () -> validator.validate(
                                LocalDate.now(),
                                null
                        )
                );

        assertEquals(
                "Start date and end date are required",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenEndDateBeforeStartDate() {

        InvalidDateRangeException exception =
                assertThrows(
                        InvalidDateRangeException.class,
                        () -> validator.validate(
                                LocalDate.of(2026, 6, 10),
                                LocalDate.of(2026, 6, 9)
                        )
                );

        assertEquals(
                "End date must not be before start date",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenDateRangeExceedsMaximumDays() {

        InvalidDateRangeException exception =
                assertThrows(
                        InvalidDateRangeException.class,
                        () -> validator.validate(
                                LocalDate.of(2026, 6, 1),
                                LocalDate.of(2026, 6, 10)
                        )
                );

        assertEquals(
                String.format(
                        "Date range must not exceed %d days",
                        ValidationConstants.MAX_DATE_RANGE_DAYS
                ),
                exception.getMessage()
        );
    }

    @Test
    void shouldPassValidationWhenDateRangeIsValid() {
        assertDoesNotThrow(
                () -> validator.validate(
                        LocalDate.of(2026, 6, 1),
                        LocalDate.of(2026, 6, 7)
                )
        );
    }
}
