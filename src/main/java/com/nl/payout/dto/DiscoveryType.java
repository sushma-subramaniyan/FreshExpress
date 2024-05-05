package com.nl.payout.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "discoveryType")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DiscoveryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long discoveryId;

    private String discoveryType;


}
