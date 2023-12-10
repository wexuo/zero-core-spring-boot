/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.example.service;

import com.zero.boot.core.service.BaseService;
import com.zero.boot.example.pojo.Example;
import com.zero.boot.example.repository.ExampleRepository;
import org.springframework.stereotype.Service;

@Service
public class ExampleService extends BaseService<Example, Long, ExampleRepository> {
}