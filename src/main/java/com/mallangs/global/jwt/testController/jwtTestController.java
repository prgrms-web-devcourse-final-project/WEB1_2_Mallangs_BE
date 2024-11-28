package com.mallangs.global.jwt.testController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class jwtTestController {

    @GetMapping("/view")
    public String view() {
        return "view";
    }
}
