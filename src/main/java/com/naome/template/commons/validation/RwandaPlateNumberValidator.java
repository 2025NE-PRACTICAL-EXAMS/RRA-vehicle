package com.naome.template.commons.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RwandaPlateNumberValidator implements ConstraintValidator<ValidPlateNumber, String> {

    private static final String PLATE_REGEX = "R[A-Z]{2}\\d{3}[A-Z]";

    @Override
    public boolean isValid(String plateNumber, ConstraintValidatorContext context) {
        // Allow null or blank values (for optional @RequestParam)
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            return true; // skip validation if not provided
        }

        return plateNumber.matches(PLATE_REGEX);
    }
}
