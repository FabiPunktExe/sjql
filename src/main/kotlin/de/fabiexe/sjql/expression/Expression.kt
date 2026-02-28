package de.fabiexe.sjql.expression

infix fun Expression.eq(other: Expression): Expression = eq(other)
infix fun Expression.neq(other: Expression): Expression = neq(other)
infix fun Expression.gt(other: Expression): Expression = gt(other)
infix fun Expression.gte(other: Expression): Expression = gte(other)
infix fun Expression.lt(other: Expression): Expression = lt(other)
infix fun Expression.lte(other: Expression): Expression = lte(other)
infix fun Expression.and(other: Expression): Expression = and(other)
infix fun Expression.or(other: Expression): Expression = or(other)
infix fun Expression.xor(other: Expression): Expression = xor(other)