package com.zero.boot.core.handler;


import com.zero.boot.core.annotation.RequestAPI;
import com.zero.boot.core.annotation.ZeroRestController;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Component
public class BaseRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

    @Override
    protected RequestMappingInfo getMappingForMethod(final Method method, final Class<?> handlerType) {
        final ZeroRestController controllerConfig = AnnotatedElementUtils.findMergedAnnotation(handlerType, ZeroRestController.class);
        if (Objects.isNull(controllerConfig) || unsupport(controllerConfig, method)) {
            return super.getMappingForMethod(method, handlerType);
        }
        final String prefix = controllerConfig.value();
        final RequestAPI api = RequestAPI.valueOf(method);
        if (Objects.isNull(prefix) || Objects.isNull(api)) {
            return null;
        }
        RequestMappingInfo info = createRequestMappingInfo(method, api);
        final RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
        if (typeInfo != null) {
            info = typeInfo.combine(info);
        }
        info = RequestMappingInfo.paths(prefix).options(this.config).build().combine(info);
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(final Method method, final RequestAPI api) {
        final RequestCondition<?> condition = getCustomMethodCondition(method);
        final String value = api.getMethod();
        final RequestMethod requestMethod = api.getRequestMethod();
        final RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(Collections.singletonList(value).toArray(new String[0])))
                .methods(requestMethod)
                .mappingName(method.getName());
        if (condition != null) {
            builder.customCondition(condition);
        }
        return builder.options(this.config).build();
    }

    private boolean unsupport(final ZeroRestController config, final Method method) {
        final RequestAPI[] apis = config.api();
        if (apis.length == 0) {
            return true;
        }
        final RequestAPI api = RequestAPI.valueOf(method);
        return api == null || Arrays.stream(apis).noneMatch(api::equals);
    }

    private RequestMappingInfo createRequestMappingInfo(final AnnotatedElement element) {
        final RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        final RequestCondition<?> condition = (element instanceof Class ?
                getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null);
    }

    @Override
    protected boolean isHandler(final Class<?> beanType) {
        return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class)) ||
                AnnotatedElementUtils.hasAnnotation(beanType, ZeroRestController.class);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void afterPropertiesSet() {

        this.config = new RequestMappingInfo.BuilderConfiguration();
        this.config.setTrailingSlashMatch(useTrailingSlashMatch());
        this.config.setContentNegotiationManager(getContentNegotiationManager());

        if (getPatternParser() != null) {
            this.config.setPatternParser(getPatternParser());
            Assert.isTrue(!this.useSuffixPatternMatch() && !this.useRegisteredSuffixPatternMatch(),
                    "Suffix pattern matching not supported with PathPatternParser.");
        } else {
            this.config.setSuffixPatternMatch(useSuffixPatternMatch());
            this.config.setRegisteredSuffixPatternMatch(useRegisteredSuffixPatternMatch());
            this.config.setPathMatcher(getPathMatcher());
        }
        this.setOrder(-1);
        super.afterPropertiesSet();
    }
}
