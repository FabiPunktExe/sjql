package de.fabiexe.sjql

import de.fabiexe.sjql.expression.Expression

infix fun UpdateStatement.where(condition: Expression): UpdateStatement = where(condition)