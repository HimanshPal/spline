/*
 * Copyright 2017 Barclays Africa Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.absa.spline.core

import za.co.absa.spline.core.model._
import org.apache.spark.sql.catalyst.expressions

/**
  * The trait represents a mapper translating Spark expressions to expressions specified by Spline library.
  */
trait ExpressionMapper extends DataTypeMapper {

  /**
    * The method translates a Spark expression to an expression specified by Spline library.
    *
    * @param sparkExpr An input Spark expression
    * @return A Spline expression
    */
  implicit def fromSparkExpression(sparkExpr: org.apache.spark.sql.catalyst.expressions.Expression): Expression = sparkExpr match {
    case a: expressions.AttributeReference => AttributeReference(a.exprId.id, a.name, fromSparkDataType(a.dataType, a.nullable))
    case bo: expressions.BinaryOperator => BinaryOperator(bo.nodeName, bo.symbol, bo.simpleString, fromSparkDataType(bo.dataType, bo.nullable), bo.children map fromSparkExpression)
    case u: expressions.ScalaUDF => UserDefinedFunction(u.udfName getOrElse u.function.getClass.getName, u.simpleString, fromSparkDataType(u.dataType, u.nullable), u.children map fromSparkExpression)
    case x => GenericExpression(x.nodeName, x.simpleString, fromSparkDataType(x.dataType, x.nullable), x.children map fromSparkExpression)
  }
}
