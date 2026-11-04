package de.fabiexe.sjql

import de.fabiexe.sjql.expression.Expression

infix fun DeleteStatement.where(condition: Expression): DeleteStatement = where(condition)