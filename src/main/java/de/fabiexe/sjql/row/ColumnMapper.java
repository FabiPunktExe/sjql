package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ColumnMapper<T> implements Function<Object, T> {
    private final Column<T> column;

    public ColumnMapper(@NotNull Class<?> type, @NotNull Column<T> column) {
        if (type.isPrimitive()) {
            type = primitiveToWrapper(type);
        }
        if (!type.isAssignableFrom(column.type())) {
            throw new IllegalArgumentException("Type " + type.getName() + " is not assignable from column type " + column.type().getName());
        }
        this.column = column;
    }

    private Class<?> primitiveToWrapper(Class<?> primitive) {
        if (primitive == boolean.class) return Boolean.class;
        if (primitive == byte.class) return Byte.class;
        if (primitive == char.class) return Character.class;
        if (primitive == double.class) return Double.class;
        if (primitive == float.class) return Float.class;
        if (primitive == int.class) return Integer.class;
        if (primitive == long.class) return Long.class;
        if (primitive == short.class) return Short.class;
        throw new IllegalArgumentException("Not a primitive type: " + primitive.getName());
    }

    @Override
    public T apply(Object o) {
        if (o == null) {
            return null;
        }
        if (column.type().isInstance(o)) {
            return column.type().cast(o);
        } else {
            throw new IllegalArgumentException("Value " + o + " is not of type " + column.type().getName());
        }
    }
}
