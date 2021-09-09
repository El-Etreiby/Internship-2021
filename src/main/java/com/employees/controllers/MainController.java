package com.employees.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/")
public class MainController {

    @GetMapping(path = "")
    @ResponseBody
    public String homePage()
    {
        return "Welcome to the website!!";
    }

}
