package com.example.backend.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

/**
 * SubscriptionRules
 */

public final class SubscriptionRules {

    public static final LocalDate RACE_DATE = LocalDate.of(2027, 3, 14);

    public static final int MIN_AGE = 14;
    public static final int MIN_AGE_10KM = 18;

    private static final int MIN_AGE_MASTER = 40;
    private static final int MIN_AGE_SENIOR = 60;

    private String SubscriptionRule() {
        return "funcção de inscrição";
    };

    public static String normalizeName(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public static String normalizeEmail(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }

    public static int calculateAge(LocalDate birth, LocalDate date_reference) {
        return Period.between(birth, date_reference).getYears();
    }

    public static Categories getCategorie(int age) {
        if (age >= MIN_AGE_SENIOR) {
            return Categories.SENIOR;
        };

        if (age >= MIN_AGE_MASTER) {
            return Categories.MASTER;
        };

        if (age >= MIN_AGE_10KM) {
            return Categories.ADULTO;
        };

        return Categories.JUVENIL;
    }


    public static SubscriptionEval eval(LocalDate birth, Length length, LocalDate race_date) {
        int age = calculateAge(birth, race_date);

        if (age < MIN_AGE) {
            return SubscriptionEval.refused(age, RegulamentException.MIN_AGE); 
        };

        if (length == Length.KM_10 && age < MIN_AGE_10KM) {
            return SubscriptionEval.refused(age, RegulamentException.DISTANCE_NOT_PERMITED);
        };

        return SubscriptionEval.accepted(age, getCategorie(age));
    }
    
    

}