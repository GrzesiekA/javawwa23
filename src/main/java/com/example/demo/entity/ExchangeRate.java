package com.example.demo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExchangeRate {

    List<Value> rates;

    public BigDecimal getValue() {
        return rates.get(0).getMid();

    }

    @Data
    static class Value {
        BigDecimal mid;

    }

}
