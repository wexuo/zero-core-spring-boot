/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.query;

import com.zero.boot.core.query.annotation.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class QueryHelper {

    public static <R, Q extends QueryAccess> Predicate getPredicate(final Root<R> root, final Q query, final CriteriaBuilder cb) {
        final List<Predicate> predicates = new ArrayList<>();
        if (query == null) {
            return cb.and(predicates.toArray(new Predicate[0]));
        }
        final Class<? extends QueryAccess> clazz = query.getClass();

        if (clazz.isAnnotationPresent(Query.class)) {
            final List<Field> fields = FieldUtils.getAllFieldsList(clazz);
            final Map<String, ? extends Class<?>> fieldNameTypeMap = fields.stream()
                    .collect(Collectors.toMap(Field::getName, Field::getType, (x, y) -> x));
            final String keyword = query.getKeyword();
            if (Objects.nonNull(keyword) && clazz.isAnnotationPresent(QueryLike.class)) {
                final QueryLike like = clazz.getAnnotation(QueryLike.class);
                getPredicate(like.value(), keyword, fieldNameTypeMap, root, cb).ifPresent(predicates::add);
            }
            if (clazz.isAnnotationPresent(QueryEqual.class)) {
                final QueryEqual equal = clazz.getAnnotation(QueryEqual.class);
                getPredicate(equal.value(), query, root, cb).ifPresent(predicates::add);
            }
            if (Objects.nonNull(keyword) && clazz.isAnnotationPresent(QueryIn.class)) {
                final QueryIn in = clazz.getAnnotation(QueryIn.class);
                // TODO 暂无需求，后期实现
            }
            final Object start = query.getStart();
            final Object end = query.getEnd();
            if (Objects.nonNull(start) && Objects.nonNull(end) && clazz.isAnnotationPresent(QueryBetween.class)) {
                final QueryBetween between = clazz.getAnnotation(QueryBetween.class);
                final String field = between.value();
                if (fieldNameTypeMap.containsKey(field)) {
                    final Expression<? extends Comparable> expression = root.get(field);
                    predicates.add(cb.between(expression, (Comparable) start, (Comparable) end));
                }
            }
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    @SneakyThrows
    private static <Q extends QueryAccess, R> Optional<Predicate> getPredicate(final String[] value, final Q query, final Root<R> root, final CriteriaBuilder cb) {
        final Set<String> keys = Arrays.stream(value).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(keys)) {
            return Optional.empty();
        }
        final List<Field> fields = FieldUtils.getAllFieldsList(query.getClass());

        final List<Predicate> predicates = new ArrayList<>(keys.size());
        for (final Field field : fields) {
            final String fieldName = field.getName();
            if (keys.contains(fieldName)) {
                final Object fieldValue = FieldUtils.readField(field, query, true);
                if (Objects.isNull(fieldValue)) {
                    continue;
                }
                final Path<Object> path = root.get(fieldName);
                if (Objects.isNull(path)) {
                    continue;
                }
                predicates.add(cb.equal(path, fieldValue));
            }
        }
        if (CollectionUtils.isEmpty(predicates)) {
            return Optional.empty();
        }
        return Optional.ofNullable(cb.and(predicates.toArray(new Predicate[0])));
    }

    private static <R> Optional<Predicate> getPredicate(final String[] fields, final String value,
                                                        final Map<String, ? extends Class<?>> fieldNameTypeMap,
                                                        final Root<R> root, final CriteriaBuilder cb) {
        if (fields.length == 0 || StringUtils.isEmpty(value)) {
            return Optional.empty();
        }
        final List<Predicate> predicates = new ArrayList<>(fields.length);
        for (final String field : fields) {
            final Class<?> clazz = fieldNameTypeMap.get(field);
            if (clazz == null) {
                continue;
            }
            final Expression<String> expression = root.get(field);
            if (Objects.nonNull(expression)) {
                predicates.add(cb.like(expression, "%" + value + "%"));
            }
        }
        if (CollectionUtils.isEmpty(predicates)) {
            return Optional.empty();
        }
        return Optional.ofNullable(cb.or(predicates.toArray(new Predicate[0])));
    }
}
