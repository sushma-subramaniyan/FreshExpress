package com.nl.payout.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    private String productName;
    private double productOfferPrice;

    private double originalPrice;

    private String originalQuantity;

    private String productQuantity;

    private long categoryId;


}
