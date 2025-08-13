package dev.snowdrop.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/greeting")
    public String greeting(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }
}

