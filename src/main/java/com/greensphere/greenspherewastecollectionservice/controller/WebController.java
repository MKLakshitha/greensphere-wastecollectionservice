package com.greensphere.greenspherewastecollectionservice.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/api/v1/waste-processing")
    public String wasteprocessing() {
        return "waste-processing";
    }

}