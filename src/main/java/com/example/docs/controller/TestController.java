package com.example.docs.controller;

import com.example.docs.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    SecurityService securityService;

    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}
