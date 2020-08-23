package com.example.demo.rest;

import com.example.demo.service.WeedGoldCombineFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
public class WeedGoldController {


    public WeedGoldCombineFacade wgFacade;

    @Autowired
    public WeedGoldController(WeedGoldCombineFacade wgFacade) {
        this.wgFacade = wgFacade;
    }


    @GetMapping(path = "/wg")
    public Map<String, BigDecimal> weedForGold () throws IOException{
        return wgFacade.weedForGold();

    }

}




