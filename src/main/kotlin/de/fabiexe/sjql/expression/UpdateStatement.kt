package de.fabiexe.sjql.expression

import de.fabiexe.sjql.UpdateStatement

infix fun UpdateStatement.where(condition: Expression): UpdateStatement = where(condition)