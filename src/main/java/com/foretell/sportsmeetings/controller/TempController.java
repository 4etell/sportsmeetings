package com.foretell.sportsmeetings.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {

    @GetMapping
    public String helloApp() {
        return "Hello SportSMeetings";
    }
}
