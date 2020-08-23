package com.example.demo.service;

import com.example.demo.dto.GoldDataDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GoldService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public BigDecimal getGold(String url){

//        GoldDataDto build = GoldDataDto.builder()
//                .cena(BigDecimal.ONE)
//                .date(new Date())
//                .build();

        return REST_TEMPLATE.getForObject(url, GoldDataDto[].class)[0].getCena();
    }

    public BigDecimal getGold2(String url) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        return new ObjectMapper().readValue(body,GoldDataDto[].class)[0].getCena();

    }
}