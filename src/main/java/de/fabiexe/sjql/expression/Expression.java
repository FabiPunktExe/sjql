package de.fabiexe.sjql.expression;

import de.fabiexe.sjql.expression.constant.DoubleExpression;
import de.fabiexe.sjql.expression.constant.IntExpression;
import de.fabiexe.sjql.expression.constant.StringExpression;
import de.fabiexe.sjql.expression.dynamic.CurrentTimestampExpression;
import org.jetbrains.annotations.NotNull;

public interface Expression {
    static @NotNull Expression constant(int value) {
        return new IntExpression(value);
    }

    static @NotNull Expression constant(double value) {
        return new DoubleExpression(value);
    }

    static @NotNull Expression constant(@NotNull String value) {
        return new StringExpression(value);
    }

    static @NotNull Expression currentTimestamp() {
        return new CurrentTimestampExpression();
    }
}
