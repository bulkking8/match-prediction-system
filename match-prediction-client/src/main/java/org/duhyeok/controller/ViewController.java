package org.duhyeok.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        // src/main/resources/templates/index.html 을 찾아가게 합니다.
        return "index";
    }
}