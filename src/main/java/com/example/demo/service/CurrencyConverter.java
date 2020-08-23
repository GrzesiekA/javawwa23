package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConverter {

    public BigDecimal convertCurrency(BigDecimal curr1, BigDecimal curr2, BigDecimal amount) {
        return amount.multiply(curr1.divide(curr2));


    }

}
