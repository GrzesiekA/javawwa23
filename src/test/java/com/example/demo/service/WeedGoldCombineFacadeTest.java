package com.example.demo.service;

import com.example.demo.entity.PricePerDay;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;


class WeedGoldCombineFacadeTest {

    private GoldService goldService = Mockito.mock(GoldService.class);
    private FileService statService = Mockito.mock(FileService.class);
    private EmailService emailService = Mockito.mock(EmailService.class);


    private WeedGoldCombineFacade wgFacade = new WeedGoldCombineFacade(goldService, statService, emailService);

    @Test
    public void shouldDoSomething() throws IOException {

        //given
        Mockito.when(goldService.getGold(ArgumentMatchers.anyString())).thenReturn(new BigDecimal(2));
        Mockito.when(statService.lowerPriceForMonth()).thenReturn(Map.of("2020-08-23",
                Optional.of(PricePerDay.builder()
                        .lowQualityPrice(new BigDecimal(10))
                        .build()
                )));

        //when
        Map<String, BigDecimal> stringBigDecimalMap = wgFacade.weedForGold();

        //then
        Assertions.assertEquals(BigDecimal.valueOf(5), stringBigDecimalMap.get("2020-08-23"));

    }
    @Test
    public void shouldSendEmail() throws IOException {
    
        //given
        Mockito.when(goldService.getGold(ArgumentMatchers.anyString())).thenReturn(BigDecimal.ZERO);
        Mockito.when(statService.lowerPriceForMonth()).thenReturn(Map.of("2020-08-23",
                Optional.of(PricePerDay.builder()
                        .lowQualityPrice(new BigDecimal(10))
                        .build()
                )));
        
        //when
        wgFacade.weedForGold();
        //then
        Mockito.verify(emailService).sendEmail();
    }
}

