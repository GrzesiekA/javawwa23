package com.example.demo.rest;

import com.example.demo.service.GoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/gold")
public class GoldRestController {

    private final GoldService goldService;

    @Autowired
    public GoldRestController(GoldService goldService) {
        this.goldService = goldService;
    }

    @GetMapping(path = "/rte")
    public BigDecimal getGoldPrice(String url){
        return goldService.getGold(url);
    }

    @GetMapping(path = "/goldprice")
    public BigDecimal getGoldPrice2(String url) throws IOException, InterruptedException {
        return goldService.getGold2(url);
    }
}