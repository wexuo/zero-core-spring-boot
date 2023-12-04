package com.zero.boot.core.service;

import com.zero.boot.core.handler.BaseRequestMappingHandlerMapping;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CoreService {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    @Resource
    private WebApplicationContext context;

    public void eps() {
        // 获取所有的 JPA Entity
        final RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        final BaseRequestMappingHandlerMapping baseHandlerMapping = context.getBean(BaseRequestMappingHandlerMapping.class);
        final List<RequestMappingHandlerMapping> mappings = Stream.of(handlerMapping, baseHandlerMapping).collect(Collectors.toList());
        for (final RequestMappingHandlerMapping mapping : mappings) {
            final Map<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodMap = mapping.getHandlerMethods();
            for (final Map.Entry<RequestMappingInfo, HandlerMethod> entry : mappingInfoHandlerMethodMap.entrySet()) {
                final RequestMappingInfo mappingInfo = entry.getKey();
                final HandlerMethod handlerMethod = entry.getValue();
                // 获取接口路径
                for (final String pattern : mappingInfo.getPatternsCondition().getPatterns()) {
                    System.out.println("接口路径：" + pattern);
                }

                // 获取请求返回类型
                final Class<?> returnType = handlerMethod.getMethod().getReturnType();
                System.out.println("请求返回类型：" + returnType);
            }
        }
    }
}
