package com.github.lnsane.web.common.controller;

import com.github.lnsane.web.common.core.exception.BadRequestException;
import com.github.lnsane.web.common.core.exception.GlobalException;
import com.github.lnsane.web.common.model.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author lnsane
 */
@RestController
public class TestController {
    @GetMapping("/")
    public void tt(){
        throw new BadRequestException();
    }

    @GetMapping("/asd")
    public BaseResponse<?> tt1(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("1","2");
        return BaseResponse.ok(hashMap);
    }

    @GetMapping("/asd2")
    public BaseResponse<?> tt21(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("1","2");
        return BaseResponse.ok(hashMap);
    }
}
