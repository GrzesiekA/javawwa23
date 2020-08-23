package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonPropertyOrder({"state", "highQualityPrice", "highCount", "midQualityPrice", "midCount", "lowQualityPrice", "lowCount", "date"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PricePerDay {

    private String state;
    private BigDecimal highQualityPrice;
    private BigDecimal midQualityPrice;
    private BigDecimal lowQualityPrice;
    private LocalDate date;
    private  Integer highCount;
    private  Integer midCount;
    private  Integer lowCount;
}
