package com.foretell.sportsmeetings.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminRestController {

    @GetMapping("hello")
    public String hello() {
<<<<<<< HEAD
        return "hello admin!";
=======
        return "Hello Admin!";
>>>>>>> develop
    }
}
