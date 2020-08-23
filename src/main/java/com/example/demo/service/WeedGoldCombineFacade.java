package com.example.demo.service;


import com.example.demo.entity.PricePerDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeedGoldCombineFacade {

    private FileService statsService;
    private GoldService goldService;
    private final EmailService emailService;

    @Autowired
    public WeedGoldCombineFacade(GoldService goldService, FileService statsService, EmailService emailService) {
        this.statsService = statsService;
        this.goldService = goldService;
        this.emailService = emailService;
    }

    public Map<String, BigDecimal> weedForGold() throws IOException {
        Map<String, Optional<PricePerDay>> statistics = statsService.lowerPriceForMonth();
        BigDecimal gold = goldService.getGold("http://api.nbp.pl/api/cenyzlota/");

        if (gold.compareTo(BigDecimal.ZERO) == 0) {
            emailService.sendEmail();
        } else {
            return statistics.entrySet().stream()
                    .filter(e -> e.getValue().isPresent())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().get().getLowQualityPrice().divide(gold, MathContext.DECIMAL32)
                    ));
        }
        return Map.of();
    }
}
