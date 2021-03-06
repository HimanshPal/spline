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

import java.util.UUID

import za.co.absa.spline.core.model.Execution
import za.co.absa.spline.core.storage.DataStorageFactory
import org.apache.spark.sql.execution.QueryExecution
import org.apache.spark.sql.util.QueryExecutionListener

/**
  * The class represents a handler listening on events that Spark triggers when an execution any action is performed. It can be considered as an entry point to Spline library.
  *
  * @param dataStorageFactory A factory of persistence layers
  */
class DataLineageListener(dataStorageFactory: DataStorageFactory) extends QueryExecutionListener {
  private lazy val dataLineageStorage = dataStorageFactory.createDataLineageStorage()
  private lazy val executionStorage = dataStorageFactory.createExecutionStorage()

  /**
    * The method is executed when an action execution is successful.
    *
    * @param funcName   A name of the executed action.
    * @param qe         A Spark object holding lineage information (logical, optimized, physical plan)
    * @param durationNs Duration of the action execution [nanoseconds]
    */
  def onSuccess(funcName: String, qe: QueryExecution, durationNs: Long): Unit = {
    processQueryExecution(funcName, qe)
  }

  /**
    * The method is executed when an error occurs during an action execution.
    *
    * @param funcName  A name of the executed action.
    * @param qe        A Spark object holding lineage information (logical, optimized, physical plan)
    * @param exception An exception describing the reason of the error
    */
  def onFailure(funcName: String, qe: QueryExecution, exception: Exception): Unit = {
    processQueryExecution(funcName, qe)
  }

  private def processQueryExecution(funcName: String, qe: QueryExecution): Unit = {
    if (funcName == "save") {
      val lineage = DataLineageHarvester.harvestLineage(qe)
      val lineageId = dataLineageStorage.exists(lineage) match {
        case None =>
          dataLineageStorage.store(lineage)
          lineage.id
        case Some(x) => x
      }

      val execution = Execution(UUID.randomUUID(), lineageId, qe.sparkSession.sparkContext.applicationId, System.currentTimeMillis())
      executionStorage.store(execution)
    }
  }
}
