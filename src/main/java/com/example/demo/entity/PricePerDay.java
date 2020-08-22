package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class PricePerDay {

    private String state;
    private BigDecimal highQualityPrice;
    private BigDecimal midQualityPrice;
    private BigDecimal lowQualityPrice;
    private LocalDate date;



}
