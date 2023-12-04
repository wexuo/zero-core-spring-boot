package com.zero.boot.core.query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class QueryHelper {

    public static <R, Q extends QueryAccess> Predicate getPredicate(final Root<R> root, final Q query, final CriteriaBuilder cb) {
        final List<Predicate> predicates = new ArrayList<>();
        if (query == null) {
            return cb.and(predicates.toArray(new Predicate[0]));
        }
        try {
            final List<Field> fields = FieldUtils.getFieldsListWithAnnotation(query.getClass(), DataQuery.class);
            for (final Field field : fields) {
                final boolean accessible = field.isAccessible();
                field.setAccessible(true);
                final DataQuery annotation = field.getAnnotation(DataQuery.class);
                final String property = annotation.property();
                final DataQuery.Type type = annotation.type();
                final String propertyName = StringUtils.isBlank(property) ? field.getName() : property;
                final Object value = field.get(query);
                if (Objects.isNull(value) || StringUtils.isEmpty(String.valueOf(value))) {
                    continue;
                }
                final String[] names = propertyName.split(",");
                for (final String name : names) {
                    final Predicate predicate = getPredicate(root, cb, type, name, field.getType(), value);
                    if (Objects.nonNull(predicate)) {
                        predicates.add(predicate);
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (final Exception e) {

        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private static <T, R> Predicate getPredicate(final Root<R> root, final CriteriaBuilder cb, final DataQuery.Type type, final String propertyName, final Class<?> clazz, final Object value) throws InterruptedException {
        final Expression<T> expression = root.get(propertyName);
        if (DataQuery.Type.EQUAL == type) {
            final Expression<? extends Comparable> as = expression.as((Class<? extends Comparable>) clazz);
            return cb.equal(as, value);
        }
        if (DataQuery.Type.NOT_EQUAL == type) {
            final Expression<? extends Comparable> as = expression.as((Class<? extends Comparable>) clazz);
            return cb.notEqual(as, value);
        }
        if (DataQuery.Type.LIKE == type) {
            final Expression<String> as = expression.as(String.class);
            return cb.like(as, "%" + value + "%");
        }
        if (DataQuery.Type.IN == type) {
            if (value instanceof String) {
                final Set<Object> objects = Arrays.stream(((String) value).split(",")).map(o -> (Object) o).collect(Collectors.toSet());
                return expression.in(objects);
            }
            return expression.in((Collection<Object>) value);
        }
        if (DataQuery.Type.NOT_IN == type) {
            if (value instanceof String) {
                final Set<Object> objects = Arrays.stream(((String) value).split(",")).map(o -> (Object) o).collect(Collectors.toSet());
                return expression.in(objects).not();
            }
            return expression.in((Collection<Object>) value).not();
        }
        if (DataQuery.Type.BETWEEN == type) {
            final List<Object> between = new ArrayList<>((List<Object>) value);
            if (CollectionUtils.isEmpty(between)) {
                return null;
            }
            final Expression<? extends Comparable> as = expression.as((Class<? extends Comparable>) between.get(0).getClass());
            return cb.between(as, (Comparable) between.get(0), (Comparable) between.get(1));
        }
        throw new InterruptedException("unsupport query type, " + type);
    }
}
