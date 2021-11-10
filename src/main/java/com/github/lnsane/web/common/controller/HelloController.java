package com.github.lnsane.web.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lnsane
 */
@RestController
public class HelloController {
    @GetMapping("/2")
    public void tt(){
        if ( 1/ 0 == 1) {

        }
    }
}
