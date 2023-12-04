package com.zero.boot.core.controller;


import com.zero.boot.core.data.Result;
import com.zero.boot.core.service.CoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class CoreController {

    @Resource
    private CoreService coreService;

    @GetMapping("/eps")
    public Result getEps() {
        coreService.eps();
        return Result.ok();
    }
}
