package com.foretell.sportsmeetings.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminRestController {

    @GetMapping("hello")
    public String hello() {
        return "Hello Admin lol!";
    }

    @GetMapping("Bye")
    public String bye() {
        return "bye Admin!";
    }
}
