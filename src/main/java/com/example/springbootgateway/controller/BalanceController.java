package com.example.springbootgateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BalanceController {

    @GetMapping("/api/balance")
    public Map<String, String> getBalance()
    {
        System.out.println("/API/BALANCE LOG");

        HashMap<String, String> map = new HashMap<>();
        map.put("account", "72367989");
        map.put("name", "Account test");
        map.put("customer", "Joao");
        map.put("amount", "2300");
        map.put("currency", "EUR");
        return map;
    }
}
