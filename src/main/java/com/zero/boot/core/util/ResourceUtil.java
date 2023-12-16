/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.util;

import lombok.extern.slf4j.Slf4j;
import sun.security.action.GetPropertyAction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AccessController;

@Slf4j
public class ResourceUtil {
    private static final String LINE_SEPARATOR = AccessController.doPrivileged(new GetPropertyAction("line.separator"));

    public static String getResource(final String path) {

        final ClassLoader loader = ResourceUtil.class.getClassLoader();
        final InputStream stream = loader.getResourceAsStream(path);
        if (stream != null) {
            try {
                final StringBuilder result = new StringBuilder();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    result.append(LINE_SEPARATOR);
                }
                stream.close();
                return result.toString();
            } catch (final Exception e) {
                log.error("getResource error", e);
            }
        }
        return null;
    }
}
