/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.example.repository;

import com.zero.boot.core.repository.BaseRepository;
import com.zero.boot.example.pojo.Example;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends BaseRepository<Example, Long> {
}