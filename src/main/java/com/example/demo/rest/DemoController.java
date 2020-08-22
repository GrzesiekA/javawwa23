package com.example.demo.rest;

import com.example.demo.entity.PricePerDay;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

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


//    @GetMapping("/price2")
//    public List<PricePerDay> priceContent2 () throws IOException {
//        return fileService.readPrices2();
//    }

}
