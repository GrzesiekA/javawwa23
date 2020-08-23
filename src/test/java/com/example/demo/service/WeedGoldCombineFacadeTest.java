package com.example.demo.service;

import com.example.demo.entity.PricePerDay;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    private CurrencyConverter currencyConverter = Mockito.mock(CurrencyConverter.class);
    private CurrencyRetriever currencyRetriever = Mockito.mock(CurrencyRetriever.class);


    private WeedGoldCombineFacade wgFacade = new WeedGoldCombineFacade(goldService, statService, emailService, currencyConverter);

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
        Mockito.when(goldService.getGold(ArgumentMatchers.anyString())).thenReturn(BigDecimal.ONE);
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

    @ParameterizedTest
    @ValueSource(doubles = {0, 0.5, 0.7, 0.9794854})
    public void shouldSendEmail2(Double price) throws IOException {

        //given
        Mockito.when(goldService.getGold(ArgumentMatchers.anyString())).thenReturn(BigDecimal.valueOf(price));

        //when
        wgFacade.weedForGold();
        //then
        Mockito.verify(emailService).sendEmail();
    }
}

