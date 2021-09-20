package com.example.springbootgateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfidentialityController {

    @GetMapping("/confidentiality")
    public Map<String, String> getConfDetails()
    {
        System.out.println("/CONF LOG");
        HashMap<String, String> map = new HashMap<>();
        map.put("fieldsToMask", "name,customer");
        return map;
    }
}
