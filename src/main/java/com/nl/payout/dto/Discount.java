package com.nl.payout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "discount")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long discountID;

    private String discountType;

    private long discountValue;

    private long minQuantity;

    private long maxQuantity;

    private Date startDate;

    private Date endDate;

}
