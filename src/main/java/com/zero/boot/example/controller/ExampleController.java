/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.example.controller;

import com.zero.boot.core.annotation.ResponseResult;
import com.zero.boot.core.controller.BaseController;
import com.zero.boot.example.pojo.Example;
import com.zero.boot.example.query.ExampleQuery;
import com.zero.boot.example.service.ExampleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseResult
@RequestMapping("/example")
public class ExampleController extends BaseController<Example, Long, ExampleQuery, ExampleService> {
}