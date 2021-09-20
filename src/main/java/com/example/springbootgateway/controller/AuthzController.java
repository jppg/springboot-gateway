package com.example.springbootgateway.controller;

import groovy.json.JsonBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthzController {

    @GetMapping("/authz")
    public ResponseEntity<Object> validateAuth()
    {
        System.out.println("/AUTHZ LOG");
        HashMap<String, String> map = new HashMap<>();
        map.put("allow", "true");

        return ResponseEntity.ok(new JsonBuilder(map).getContent());
        //return ResponseEntity.status(404).build();
    }

}
