package com.db.enums;

import lombok.Getter;

@Getter

public enum ServicesCategories {
    ElECTRICIAN("Electrician"),
    PLUMBING("Plumbing"),
    CAR_PENTER("Car Penter"),
    CLEANING("Cleaning");


        private final String value;
    ServicesCategories(String value) {
        this.value=value;
    }
}
