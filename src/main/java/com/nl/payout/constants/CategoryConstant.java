package com.nl.payout.constants;

import java.util.Arrays;

public enum CategoryConstant {

    VEGETABLE(1),
    BAKERY(2),
    // ...
    BEVERAGE(3);

    public final long categoryId;


    private CategoryConstant(long categoryId) {
        this.categoryId = categoryId;
    }

    public static CategoryConstant getCategoryByValue(long value) {
        return Arrays.stream(CategoryConstant.values())
                .filter(category -> category.categoryId == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with value " + value));
    }



}
