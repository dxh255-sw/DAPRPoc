package com.sw.dapr.daprpoc.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SWDaprController
{

    @PostMapping("/dapr/HelloWorld")
    @ResponseBody
    public static ResponseEntity sendName(@RequestBody String body)
    {
        System.out.println("Hello World!!!!!");
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
