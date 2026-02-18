package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class ConstructorRowMapper<T> implements Function<ReadableRow<T>, T> {
    private final Column<?>[] columns;
    private final Constructor<T> constructor;
    private final ColumnMapper<?>[] columnMappers;

    @SuppressWarnings("unchecked")
    public ConstructorRowMapper(@NotNull Class<T> type, @NotNull Column<?> @NotNull [] columns) {
        Class<?>[] columnTypes = new Class<?>[columns.length];
        for (int i = 0; i < columns.length; i++) {
            columnTypes[i] = columns[i].type();
        }

        Constructor<T> constructor = null;
        ColumnMapper<?>[] columnMappers = null;
        for (Constructor<?> c : type.getConstructors()) {
            Class<?>[] parameterTypes = c.getParameterTypes();
            if (parameterTypes.length != columns.length) {
                continue;
            }
            try {
                ColumnMapper<?>[] mappers = new ColumnMapper<?>[columns.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    mappers[i] = new ColumnMapper<>(parameterTypes[i], columns[i]);
                }
                constructor = (Constructor<T>) c;
                columnMappers = mappers;
                break;
            } catch (IllegalArgumentException _) {}
        }

        if (constructor == null) {
            String[] parameterTypeNames = new String[columnTypes.length];
            for (int i = 0; i < columnTypes.length; i++) {
                parameterTypeNames[i] = columnTypes[i].getName();
            }
            throw new IllegalArgumentException("Constructor not found: " + type.getName() + "(" + String.join(", ", parameterTypeNames) + ")");
        }

        this.columns = columns;
        this.constructor = constructor;
        this.columnMappers = columnMappers;
    }

    @Override
    public T apply(ReadableRow<T> row) {
        Object[] values = new Object[columns.length];
        for (int i = 0; i < columns.length; i++) {
            values[i] = columnMappers[i].apply(row.get(columns[i]));
        }
        try {
            return constructor.newInstance(values);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
