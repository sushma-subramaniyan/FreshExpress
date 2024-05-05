package com.nl.payout.model.request;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {


    private long productId;
    private Double productPrice;
    private String productName;
    private long productWeight;
    private long productQuantity;

    private long categoryId;
}
