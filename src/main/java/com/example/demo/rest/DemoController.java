package com.example.demo.rest;

import com.example.demo.entity.PricePerDay;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DemoController {

    private final FileService fileService;

    @Autowired
    public DemoController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String fileContent() throws IOException {
        return fileService.fileContent();
    }

    @GetMapping("/price")
    public List<PricePerDay> priceContent() throws IOException {
        return fileService.readPrices();
    }

    @GetMapping("/stats")
    public PricePerDay statistics() throws IOException {
        return fileService.statistics();
    }

    @GetMapping("/statsForCountry")
    public Map<String, Optional<PricePerDay>> statsForCountry() throws IOException {
        return fileService.statisticsForCountry();
    }

    @GetMapping("/statsForDay")
    public Map<LocalDate, Optional<PricePerDay>> statsForDay() throws IOException {
        return fileService.lowerPriceForDay();
    }

    @GetMapping("/statsForMonth")
    public Map<String, Optional<PricePerDay>> statsForMonth() throws IOException {
        return fileService.lowerPriceForMonth();
    }

    @GetMapping("/allPrices")
    public BigDecimal allPrices() throws IOException {
        return fileService.sumLowPrices();
    }

    @GetMapping("/sumPerDay")
    public Map<LocalDate, BigDecimal> sumPerDay() throws IOException {
        return fileService.sumAllPricesPerDay();
    }

//    @GetMapping("/price2")
//    public List<PricePerDay> priceContent2 () throws IOException {
//        return fileService.readPrices2();
//    }

}
