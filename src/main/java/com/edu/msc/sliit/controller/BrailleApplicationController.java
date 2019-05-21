package com.edu.msc.sliit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrailleApplicationController {
	@GetMapping(value="/")
	public String homepage(){
        return "index";
    }
}
