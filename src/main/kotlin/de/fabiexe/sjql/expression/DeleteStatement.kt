package de.fabiexe.sjql.expression

import de.fabiexe.sjql.DeleteStatement

infix fun DeleteStatement.where(condition: Expression): DeleteStatement = where(condition)