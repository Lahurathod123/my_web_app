package com.llb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String hello() {
        // redirect the user to /dashboard.html in the static folder
        return "redirect:/dashboard.html";
    }
}
