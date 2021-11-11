package com.github.lnsane.web.common;

import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;


/**
 * @author lnsane
 */
//@SpringBootApplication
//@EnableScheduling
@ComponentScan
public class App {

//    public static void main(String[] args) {
//
//        SpringApplication.run(App.class, args);
//    }

    @PostConstruct
    public void hello(){
    }

}
