package com.example.demo.service;


import com.example.demo.dto.GoldDataDto;
import com.example.demo.entity.ExchangeRate;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class CurrencyRetriever {

    private static final String URL = "http://api.nbp.pl/api/exchangerates/rates/a/";
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public BigDecimal retrieveCurrentExchangeRate (String currency){

            return REST_TEMPLATE.getForObject(URL + currency, ExchangeRate.class)
                    .getValue();
        }
    }

