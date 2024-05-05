package com.nl.payout.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "currency")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long currencyId;

    private String currencyCode;

    private String currencyName;

    private String symbol;
}
